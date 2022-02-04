package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import dev.chargedbyte.reaktor_summer_2022.feature.game.routing.GamesPagedResponse
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import dev.chargedbyte.reaktor_summer_2022.feature.player.PlayerStatsDto
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(KtorExperimentalLocationsAPI::class)
class PlayerRoutes @Inject constructor(
    application: Application, playerService: PlayerService, gameService: GameService
) {
    init {
        application.routing {
            authenticate("api") {
                get<Player> {
                    val player = playerService.findById(it.id)
                    if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
                }

                get<PlayerStats> {
                    val total = gameService.countGamesByPlayerId(it.id)
                    val wins = gameService.countGamesByPlayerIdAndWon(it.id)
                    val mostPlayed = gameService.mostPlayedHandByPlayerId(it.id)

                    val winRatio = (wins.toDouble() / total.toDouble())

                    call.respond(PlayerStatsDto(wins, total, winRatio, mostPlayed))
                }

                get<PlayerGamesPaged> { request ->
                    if (request.size > 150) {
                        call.respond(HttpStatusCode.BadRequest, "Page size cannot exceed 150")
                        return@get
                    }

                    val (games, total) = gameService.findGamesByPlayerIdPaged(request.id, request.size, request.page)

                    call.respond(GamesPagedResponse(total, newSuspendedTransaction { games.map { it.toDto() } }))
                }

                get<SearchPlayers> { request ->
                    val players = playerService.searchPlayers(request.query)
                    call.respond(players.map { it?.toDto() })
                }

                get<PlayerByName> {
                    val player = playerService.findByName(it.name)
                    if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
