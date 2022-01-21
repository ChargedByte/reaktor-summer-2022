package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.utils.databaseQuery
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor() : PlayerService {
    override suspend fun findById(id: Int) = databaseQuery { Player.findById(id) }

    override suspend fun findByName(name: String) = databaseQuery { Player.find { Players.name eq name }.firstOrNull() }

    override suspend fun findByNameOrCreate(name: String): Player {
        var player = findByName(name)

        if (player == null) {
            player = databaseQuery {
                Player.new {
                    this.name = name
                }
            }
        }

        return player
    }
}
