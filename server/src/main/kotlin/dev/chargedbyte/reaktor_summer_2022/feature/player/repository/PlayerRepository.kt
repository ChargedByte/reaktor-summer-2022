package dev.chargedbyte.reaktor_summer_2022.feature.player.repository

import dev.chargedbyte.reaktor_summer_2022.core.repository.CrudRepository
import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import org.litote.kmongo.Id

interface PlayerRepository : CrudRepository<Player, Id<Player>>
