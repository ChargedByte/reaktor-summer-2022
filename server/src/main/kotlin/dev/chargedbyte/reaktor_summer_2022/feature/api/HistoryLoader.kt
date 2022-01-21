package dev.chargedbyte.reaktor_summer_2022.feature.api

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.History
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import javax.inject.Inject

class HistoryLoader @Inject constructor(private val client: HttpClient, private val gameService: GameService) {
    private val logger = LoggerFactory.getLogger(HistoryLoader::class.java)

    private val scope = CoroutineScope(Dispatchers.IO)

    private var etag: String? = null

    private val completeCursors = mutableListOf<String>()

    init {
        scope.launch {
            while (true) {
                execute()
                delay(125)
            }
        }
    }

    private suspend fun execute(cursor: String? = null) {
        if (cursor != null) completeCursors.add(cursor)

        val response = get(cursor)

        if (cursor == null) {
            val responseETag = response.etag()
            if (responseETag == etag) return
            etag = responseETag
        }

        val history = response.receive<History>()

        gameService.saveAll(history.data)

        logger.info("Executed fetch on cursor: ${cursor ?: "/rps/history"}")

        if (!completeCursors.contains(history.cursor)) {
            execute(history.cursor)
        }
    }

    private suspend fun get(cursor: String? = null) =
        client.get<HttpResponse>("https://bad-api-assignment.reaktor.com${cursor ?: "/rps/history"}")
}
