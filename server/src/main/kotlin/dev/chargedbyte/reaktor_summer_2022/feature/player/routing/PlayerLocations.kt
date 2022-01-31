package dev.chargedbyte.reaktor_summer_2022.feature.player.routing

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER)
data class Player(val id: Int)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_STATS)
data class PlayerStats(val id: Int)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_GAMES)
data class PlayerGamesPaged(val id: Int, val size: Int, val page: Long = 0)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.SEARCH_PLAYERS)
data class SearchPlayers(val query: String)

@KtorExperimentalLocationsAPI
@Location(PlayerConstant.PLAYER_BY_NAME)
data class PlayerByName(val name: String)
