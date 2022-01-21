package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.slf4j.LoggerFactory
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor() : PlayerService {
    private val logger = LoggerFactory.getLogger(PlayerServiceImpl::class.java)

    override suspend fun findByIdAsync(id: Int) = suspendedTransactionAsync { Player.findById(id) }

    override suspend fun findByNameAsync(name: String) =
        suspendedTransactionAsync { Player.find { Players.name eq name }.firstOrNull() }

    override suspend fun findByNameOrCreateAsync(name: String) = suspendedTransactionAsync {
        var player = findByNameAsync(name).await()

        if (player == null) {
            player = suspendedTransactionAsync {
                Player.new {
                    this.name = name
                }
            }.await()

            logger.info("Created a new player: ${player.name}")
        }

        return@suspendedTransactionAsync player
    }
}
