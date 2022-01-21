package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import kotlinx.coroutines.Deferred

interface GameService {
    suspend fun findAllPagedAsync(size: Int, page: Long): Deferred<Pair<List<Game>, Long>>
    suspend fun saveAllAsync(games: List<ApiGame>): Deferred<Unit>
    suspend fun findByIdAsync(id: String): Deferred<Game?>
    suspend fun existsById(id: String): Boolean
    suspend fun count(): Long
    suspend fun findGamesByPlayerIdAsync(playerId: Int): Deferred<List<Game>>
    suspend fun findGamesWonByPlayerIdAsync(playerId: Int): Deferred<List<Game>>
}
