package dev.chargedbyte.reaktor_summer_2022.feature.game.routing

import dev.chargedbyte.reaktor_summer_2022.feature.game.GameDto
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(KtorExperimentalLocationsAPI::class)
class GameRoutes @Inject constructor(application: Application, gameService: GameService) {
    init {
        application.routing {
            get<GamesPaged> { request ->
                if (request.size > 150) {
                    call.respond(HttpStatusCode.BadRequest, "Page size cannot exceed 150")
                    return@get
                }

                val (games, total) = gameService.findAllPagedAsync(request.size, request.page).await()

                if (gameService.count() == 0L || request.page >= total) {
                    call.respond(HttpStatusCode.NoContent)
                    return@get
                }

                call.respond(
                    HttpStatusCode.OK, GamesPagedResponse(total, newSuspendedTransaction { games.map { it.toDto() } })
                )
            }
        }
    }
}

private data class GamesPagedResponse(val totalPages: Long, val games: List<GameDto>)