package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game

interface GameService {
    suspend fun saveAll(games: List<ApiGame>)
    suspend fun findById(id: String): Game?
    suspend fun existsById(id: String): Boolean
}
