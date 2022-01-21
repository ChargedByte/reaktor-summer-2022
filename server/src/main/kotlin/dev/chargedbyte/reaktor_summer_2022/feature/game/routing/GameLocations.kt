package dev.chargedbyte.reaktor_summer_2022.feature.game.routing

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(GameConstant.GAMES_PAGED)
class GamesPaged(val size: Int, val page: Long = 0)
