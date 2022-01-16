package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.game.repository.GameRepository
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private val repository: GameRepository) : GameService
