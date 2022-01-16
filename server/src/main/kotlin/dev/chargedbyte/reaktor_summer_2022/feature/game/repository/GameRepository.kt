package dev.chargedbyte.reaktor_summer_2022.feature.game.repository

import dev.chargedbyte.reaktor_summer_2022.core.repository.CrudRepository
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game

interface GameRepository : CrudRepository<Game, String>
