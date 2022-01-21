package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import kotlinx.coroutines.Deferred

interface PlayerService {
    suspend fun findByIdAsync(id: Int): Deferred<Player?>
    suspend fun findByNameAsync(name: String): Deferred<Player?>
    suspend fun findByNameOrCreateAsync(name: String): Deferred<Player>
}
