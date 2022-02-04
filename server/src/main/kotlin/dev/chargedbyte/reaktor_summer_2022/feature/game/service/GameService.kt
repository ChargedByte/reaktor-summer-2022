package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.model.Hand
import java.time.Duration

interface GameService {
    suspend fun findAllGameIds(): HashSet<String>
    suspend fun findAllPaged(size: Int, page: Long): Pair<List<Game>, Long>
    suspend fun findGamesByPlayerIdPaged(playerId: Int, size: Int, page: Long): Pair<List<Game>, Long>
    suspend fun saveAll(cursor: String, fetchDuration: Duration, games: List<ApiGame>)
    suspend fun findById(id: String): Game?
    suspend fun existsById(id: String): Boolean
    suspend fun count(): Long
    suspend fun countGamesByPlayerId(playerId: Int): Long
    suspend fun countGamesByPlayerIdAndWon(playerId: Int): Long
    suspend fun countGamesByPlayerIdWhereHandWasPlayed(playerId: Int, hand: Hand): Long
    suspend fun mostPlayedHandByPlayerId(playerId: Int): Hand
}
