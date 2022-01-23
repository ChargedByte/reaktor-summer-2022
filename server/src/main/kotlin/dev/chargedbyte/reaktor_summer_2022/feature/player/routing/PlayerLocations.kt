package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER)
class Player(val id: Int)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_BY_NAME)
class PlayerByName(val name: String)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_STATS)
class PlayerStats(val id: Int)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_GAMES)
class PlayerGamesPaged(val id: Int, val size: Int, val page: Long = 0)
