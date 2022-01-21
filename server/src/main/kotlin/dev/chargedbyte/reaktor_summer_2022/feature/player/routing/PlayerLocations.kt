package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_BY_ID)
class PlayerById(val id: Int)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_BY_NAME)
class PlayerByName(val name: String)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_STATS)
class PlayerStats(val id: Int)
