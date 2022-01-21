package dev.chargedbyte.reaktor_summer_2022.feature.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import dev.chargedbyte.reaktor_summer_2022.feature.api.model.History
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private var parent = Job()

    private val scope = CoroutineScope(Dispatchers.IO + parent)

    private var rateLimitLimit: Int? = null
    private val rateLimitRemaining = AtomicInteger(500)
    private val rateLimitReset = AtomicLong(0)

    private lateinit var completed: MutableMap<String, String?>

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() = runBlocking {
                parent.cancel()

                val mapper = ObjectMapper()
                val completedFile = File("completed.json")

                mapper.writeValue(completedFile, completed)
            }
        })

        val typeRef = object : TypeReference<MutableMap<String, String?>>() {}

        val mapper = ObjectMapper()
        val completedFile = File("completed.json")

        completed = if (completedFile.exists()) {
            mapper.readValue(completedFile, typeRef)
        } else {
            mutableMapOf()
        }

        scope.launch {
            while (true) {
                val start = Instant.now()

                launch { execute() }.join()

                val end = Instant.now()

                val duration = Duration.between(start, end)
                logger.info("HistoryLoader completed in $duration")
            }
        }
    }

    private suspend fun execute(cursor: String = "/rps/history") {
        logger.info("Starting fetch on cursor: $cursor")

        // Let's wait for the rate limit to reset
        if (rateLimitRemaining.get() <= 0) {
            val reset = Instant.ofEpochSecond(rateLimitReset.get())
            if (reset.isAfter(Instant.now())) {
                val duration = Duration.between(reset, Instant.now())

                logger.info("Rate limited, waiting for ${duration.toMillis()}ms before continuing")

                delay(duration.toMillis())
            }
        }

        val response = get(cursor)

        // We are relying on weak ETags here
        if (completed[cursor] == response.etag()) {
            return
        }
        completed[cursor] = response.etag()

        // We are assuming that the rate limit headers are always present

        // This is technically a waste of time, as long as the rate limit max value doesn't change
        val rateLimitLimitHeader = response.headers["X-Ratelimit-Limit"]
        if (rateLimitLimitHeader != null) {
            val i = rateLimitLimitHeader.toInt()
            if (i != rateLimitLimit) {
                rateLimitLimit = i
                rateLimitRemaining.set(rateLimitLimit!!)
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

        logger.info("Executed fetch on cursor: $cursor")

        if (history.cursor != null) {
            execute(history.cursor)
        }

        gameService.saveAll(history.data)
    }

    private suspend fun get(cursor: String) = client.get<HttpResponse>("https://bad-api-assignment.reaktor.com$cursor")
}
