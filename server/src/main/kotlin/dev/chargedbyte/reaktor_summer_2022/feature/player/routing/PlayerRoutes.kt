package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import dev.chargedbyte.reaktor_summer_2022.feature.player.PlayerStatsDto
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import javax.inject.Inject

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(KtorExperimentalLocationsAPI::class)
class PlayerRoutes @Inject constructor(
    application: Application, playerService: PlayerService, gameService: GameService
) {
    init {
        application.routing {
            get<PlayerById> {
                val player = playerService.findById(it.id)
                if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
            }

            get<PlayerByName> {
                val player = playerService.findByName(it.name)
                if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
            }

            get<PlayerStats> {
                val total = gameService.countGamesByPlayerId(it.id)
                val wins = gameService.countGamesByPlayerIdAndWon(it.id)
                val mostPlayed = gameService.mostPlayedHandByPlayerId(it.id)

                val winRatio = (wins / total * 1.0)

                PlayerStatsDto(wins, total, winRatio, mostPlayed)
            }
        }
    }
}
