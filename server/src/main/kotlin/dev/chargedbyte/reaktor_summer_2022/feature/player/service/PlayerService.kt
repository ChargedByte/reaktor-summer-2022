package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player

interface PlayerService {
    suspend fun findById(id: Int): Player?
    suspend fun findByName(name: String): Player?
    suspend fun findByNameOrCreate(name: String): Player
}
