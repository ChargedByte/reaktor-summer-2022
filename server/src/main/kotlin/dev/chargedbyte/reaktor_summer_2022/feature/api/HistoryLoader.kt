package dev.chargedbyte.reaktor_summer_2022.feature.api

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.History
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private var parent = Job()

    private val scope = CoroutineScope(Dispatchers.IO + parent)

    private val rateLimitRemaining = AtomicInteger(500)
    private val rateLimitReset = AtomicLong()

    private val jobs = Collections.synchronizedMap(mutableMapOf<String, Job?>("/rps/history" to null))
    private val etags = Collections.synchronizedMap(mutableMapOf<String, String?>())

    private val newCursors = Collections.synchronizedList(mutableListOf<String>())
    private val completedCursors = Collections.synchronizedList(mutableListOf<String>())

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() = runBlocking {
                parent.cancel()
            }
        })

        scope.launch {
            while (true) {
                if (parent.isCancelled) break

                val cursors = newCursors.iterator()
                while (cursors.hasNext()) {
                    val cursor = cursors.next().also { cursors.remove() }
                    jobs[cursor] = null
                }

                val iterator = jobs.iterator()
                while (iterator.hasNext()) {
                    val (cursor, job) = iterator.next()

                    if (job != null || completedCursors.contains(cursor)) continue

                    if (rateLimitRemaining.get() == 0) {
                        val reset = Instant.ofEpochSecond(rateLimitReset.get())
                        val duration = Duration.between(Instant.now(), reset)

                        logger.warn("Rate limited, waiting for $duration before continuing")

                        delay(duration.toMillis() + 1)

                        rateLimitRemaining.set(500)
                    }

                    jobs[cursor] = launch { fetch(cursor) }.also { it.invokeOnCompletion { jobs[cursor] = null } }

                    rateLimitRemaining.decrementAndGet()
                }

                jobs.values.forEach { it?.join() }

                delay(125)
            }
        }
    }

    private suspend fun fetch(cursor: String) {
        val startTime = Instant.now()

        val response = try {
            get(cursor, etags[cursor])
        } catch (ex: RedirectResponseException) {
            if (ex.response.status == HttpStatusCode.NotModified) {
                return
            }
            throw ex
        } catch (ex: Exception) {
            logger.error("Failed fetch on cursor $cursor, adding to the queue for retry", ex)
            newCursors.add(cursor)
            return
        }

        etags[cursor] = response.etag()

        val rateLimitResetHeader = response.headers["X-Ratelimit-Reset"]
        if (rateLimitResetHeader != null) {
            rateLimitReset.set(rateLimitResetHeader.toLong())
        }

        val history = response.receive<History>()

        val duration = Duration.between(startTime, Instant.now())

        logger.debug("Completed fetch on cursor $cursor in $duration")

        if (history.cursor != null && !jobs.containsKey(history.cursor)) {
            newCursors.add(history.cursor)
        }

        gameService.saveAll(cursor, duration, history.data)

        if (cursor != "/rps/history") {
            completedCursors.add(cursor)
        }
    }

    private suspend fun get(cursor: String, etag: String?) =
        client.get<HttpResponse>("https://bad-api-assignment.reaktor.com$cursor") {
            if (etag != null) {
                header("If-None-Match", etag)
            }
        }
}
