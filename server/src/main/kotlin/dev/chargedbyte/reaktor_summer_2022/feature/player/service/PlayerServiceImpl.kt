package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor() : PlayerService {
    private val logger = LoggerFactory.getLogger(PlayerServiceImpl::class.java)

    override suspend fun findById(id: Int) = newSuspendedTransaction { Player.findById(id) }

    override suspend fun findByName(name: String) =
        newSuspendedTransaction { Player.find { Players.name eq name }.firstOrNull() }

    override suspend fun findByNameOrCreate(name: String) = newSuspendedTransaction {
        var player = findByName(name)

        if (player == null) {
            player = newSuspendedTransaction {
                Player.new {
                    this.name = name
                }
            }

            logger.info("Created a new player: ${player.name}")
        }

        player
    }
}
