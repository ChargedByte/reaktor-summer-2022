package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.repository.PlayerRepository
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor(private val repository: PlayerRepository) : PlayerService
