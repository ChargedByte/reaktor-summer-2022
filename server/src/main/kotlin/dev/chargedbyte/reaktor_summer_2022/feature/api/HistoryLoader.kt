package dev.chargedbyte.reaktor_summer_2022.feature.api

import dev.chargedbyte.reaktor_summer_2022.feature.api.data.ETags
import dev.chargedbyte.reaktor_summer_2022.feature.api.model.History
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private val loaderParentJob = Job()

    private val rateLimitRemaining = AtomicInteger(500)
    private val rateLimitReset = AtomicReference(Instant.now().plus(1, ChronoUnit.MINUTES))

    private val etags = Collections.synchronizedMap(mutableMapOf<String, String?>())

    init {
        setup()

        CoroutineScope(Dispatchers.IO + loaderParentJob).launch {
            start()
        }
    }

    private fun setup() {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                loaderParentJob.cancel()

                transaction {
                    ETags.deleteAll()
                    ETags.batchInsert(etags.toList()) {
                        this[ETags.id] = it.first
                        this[ETags.etag] = it.second
                    }
                }
            }
        })

        transaction {
            etags.putAll(ETags.selectAll().associate { it[ETags.id].value to it[ETags.etag] })
        }
    }

    private suspend fun start() {
        var firstRun = true
        while (true) {
            if (loaderParentJob.isCancelled) break

            val cursors =
                if (firstRun) etags.keys.toList() else etags.filter { it.key == "/rps/history" || it.value == null }.keys.toList()

            val jobs = mutableListOf<Job>()
            cursors.forEach {
                if (loaderParentJob.isCancelled) return

                if (rateLimitRemaining.get() == 0) {
                    val duration = Duration.between(rateLimitReset.get(), Instant.now())

                    logger.warn("Rate limit reached, waiting for $duration")

                    delay(duration.toMillis() + 1)

                    rateLimitRemaining.set(500)
                }

                coroutineScope { jobs.add(launch { load(it) }) }

                rateLimitRemaining.decrementAndGet()

                // This should keep us from getting rate limited, but there is always the rate limit logic in case this doesn't work
                delay(125)
            }

            if (firstRun)
                firstRun = false
        }
    }

    private suspend fun load(cursor: String) {
        if (loaderParentJob.isCancelled) return

        val startTime = Instant.now()

        val response = runCatching {
            client.get<HttpResponse>("https://bad-api-assignment.reaktor.com$cursor") {
                if (etags[cursor] != null) {
                    header("If-None-Match", etags[cursor])
                }
            }
        }.onFailure {
            if (it is RedirectResponseException && it.response.status == HttpStatusCode.NotModified) {
                logger.info(
                    "Skipping $cursor, not modified, took ${
                        Duration.between(startTime, Instant.now()).toMillis()
                    }ms"
                )
                return
            }
        }.getOrThrow()

        etags[cursor] = response.etag()

        if (response.headers["X-Ratelimit-Reset"] != null) {
            val reset = Instant.ofEpochSecond(response.headers["X-Ratelimit-Reset"]!!.toLong())
            if (reset != rateLimitReset.get())
                rateLimitReset.set(reset)
        }

        val history = response.receive<History>()

        val duration = Duration.between(startTime, Instant.now())

        if (history.cursor != null && !etags.containsKey(history.cursor)) {
            etags[history.cursor] = null
        }

        logger.debug("Loaded ${history.data.size} items from $cursor in ${duration.toMillis()}ms")

        gameService.saveAll(cursor, duration, history.data)
    }

    private fun stop() = loaderParentJob.cancel()
}
