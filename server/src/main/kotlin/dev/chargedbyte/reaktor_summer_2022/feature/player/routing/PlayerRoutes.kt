package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import javax.inject.Inject

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(KtorExperimentalLocationsAPI::class)
class PlayerRoutes @Inject constructor(application: Application, playerService: PlayerService) {
    init {
        application.routing {
            get<PlayerById> {
                val player = playerService.findByIdAsync(it.id).await()
                if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
            }

            get<PlayerByName> {
                val player = playerService.findByNameAsync(it.name).await()
                if (player != null) call.respond(player.toDto()) else call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
