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
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private val loaderParentJob = Job()

    private val rateLimitRemaining = AtomicInteger(500)
    private var rateLimitReset = Instant.now().plus(1, ChronoUnit.MINUTES)

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

                coroutineScope { jobs.add(launch { load(it) }) }
            }

            if (firstRun)
                firstRun = false
        }
    }

    private suspend fun load(cursor: String) {
        if (loaderParentJob.isCancelled) return

        // If we are near the rate limit reset time, lets slow down a bit. We really don't want to hit the rate limit.
        if (rateLimitRemaining.get() < 100)
            delay(125)

        if (rateLimitRemaining.get() == 0) {
            val duration = Duration.between(Instant.now(), rateLimitReset)

            logger.warn("Rate limit reached, waiting for $duration")

            delay(duration.toMillis() + 1)
        }

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

        if (response.headers["X-Ratelimit-Reset"] != null) {
            val reset = Instant.ofEpochSecond(response.headers["X-Ratelimit-Reset"]!!.toLong())
            if (reset != rateLimitReset)
                rateLimitReset = reset
        }

        if (response.headers["X-Ratelimit-Remaining"] != null) {
            rateLimitRemaining.set(response.headers["X-Ratelimit-Remaining"]!!.toInt())
        }

        etags[cursor] = response.etag()

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
