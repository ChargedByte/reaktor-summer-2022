package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import javax.inject.Inject

val findByNameOrCreateMutex = Mutex()

class PlayerServiceImpl @Inject constructor() : PlayerService {
    private val logger = LoggerFactory.getLogger(PlayerServiceImpl::class.java)

    override suspend fun findById(id: Int) = newSuspendedTransaction { Player.findById(id) }

    override suspend fun findByName(name: String) =
        newSuspendedTransaction { Player.find { Players.name eq name }.firstOrNull() }

    override suspend fun findByNameOrCreate(name: String) = findByNameOrCreateMutex.withLock {
        var player = findByName(name)

        if (player == null) {
            player = newSuspendedTransaction {
                Player.new { this.name = name }
            }

            logger.debug("Created a new player: ${player.name}")
        }

        player
    }

    override suspend fun searchPlayers(query: String) = newSuspendedTransaction {
        val id = query.toIntOrNull()
        if (id != null) {
            return@newSuspendedTransaction listOf(Player.findById(id))
        }

        val players = Player.find { Players.name like "$query%" }.toList()

        if (players.isNotEmpty())
            return@newSuspendedTransaction players

        return@newSuspendedTransaction Player.find { Players.name like "%$query%" }.toList()
    }
}
