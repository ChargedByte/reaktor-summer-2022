package dev.chargedbyte.reaktor_summer_2022.feature.api

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.History
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private var parent = Job()

    private val scope = CoroutineScope(Dispatchers.IO + parent)

    private var rateLimitLimit = AtomicInteger()
    private val rateLimitRemaining = AtomicInteger(500)
    private val rateLimitReset = AtomicLong(0)

    private val jobs = mutableMapOf<String, Job?>("/rps/history" to null)
    private val etags = mutableMapOf<String, String?>()

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() = runBlocking {
                parent.cancel()
            }
        })

        scope.launch {
            while (true) {
                if (parent.isCancelled) break

                val iter = jobs.iterator()
                while (iter.hasNext()) {
                    val (cursor, job) = iter.next()

                    if (job != null) continue

                    jobs[cursor] = fetch(cursor)
                }

                delay(125)
            }
        }
    }

    private suspend fun fetch(cursor: String) = scope.launch {
        // Let's wait for the rate limit to reset
        if (rateLimitRemaining.get() <= 0) {
            val reset = Instant.ofEpochSecond(rateLimitReset.get())
            if (reset.isAfter(Instant.now())) {
                val duration = Duration.between(reset, Instant.now())

                logger.info("Rate limited, waiting for ${duration.toMillis()}ms before continuing")

                delay(duration.toMillis())
            }
        }

        val startTime = Instant.now()

        val response = runCatching { get(cursor) }.getOrElse {
            if (parent.isCancelled) return@launch

            logger.info("Failed fetch on cursor $cursor, retrying")
            return@launch
        }

        // We are relying on weak ETags here
        if (etags[cursor] == response.etag()) {
            return@launch
        }
        etags[cursor] = response.etag()

        // We are assuming that the rate limit headers are always present

        if (rateLimitLimit.get() == 0) {
            val rateLimitLimitHeader = response.headers["X-Ratelimit-Limit"]
            if (rateLimitLimitHeader != null) {
                val i = rateLimitLimitHeader.toInt()
                if (i != rateLimitLimit.get()) {
                    rateLimitLimit.set(i)
                }
            }
        }

        val rateLimitRemainingHeader = response.headers["X-Ratelimit-Remaining"]
        if (rateLimitRemainingHeader != null) {
            rateLimitRemaining.set(rateLimitRemainingHeader.toInt())
        }

        val rateLimitResetHeader = response.headers["X-Ratelimit-Reset"]
        if (rateLimitResetHeader != null) {
            rateLimitReset.set(rateLimitResetHeader.toLong())
        }

        val history = response.receive<History>()

        val duration = Duration.between(startTime, Instant.now())

        logger.debug("Completed fetch on cursor $cursor in $duration")

        if (history.cursor != null && !jobs.containsKey(history.cursor)) {
            jobs[history.cursor] = null
        }

        launch { gameService.saveAll(cursor, duration, history.data) }.join()
    }

    private suspend fun get(cursor: String) = client.get<HttpResponse>("https://bad-api-assignment.reaktor.com$cursor")
}
