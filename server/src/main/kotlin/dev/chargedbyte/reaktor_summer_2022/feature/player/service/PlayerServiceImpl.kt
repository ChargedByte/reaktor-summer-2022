package dev.chargedbyte.reaktor_summer_2022.feature.player.service

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.utils.suspendedDatabaseQuery
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import javax.inject.Inject

val findByNameOrCreateMutex = Mutex()

class PlayerServiceImpl @Inject constructor() : PlayerService {
    private val logger = LoggerFactory.getLogger(PlayerServiceImpl::class.java)

    override suspend fun findById(id: Int) = suspendedDatabaseQuery { Player.findById(id) }

    override suspend fun findByName(name: String) =
        suspendedDatabaseQuery { Player.find { Players.name eq name }.firstOrNull() }

    override suspend fun findByNameOrCreate(name: String) = findByNameOrCreateMutex.withLock {
        var player = findByName(name)

        if (player == null) {
            player = suspendedDatabaseQuery {
                Player.new {
                    this.name = name
                }
            }

            logger.debug("Created a new player: ${player.name}")
        }

        player
    }

    override suspend fun searchPlayers(query: String) = suspendedDatabaseQuery {
        val id = query.toIntOrNull()
        if (id != null) {
            return@suspendedDatabaseQuery listOf(Player.findById(id))
        }

        val players = Player.find { Players.name like "$query%" }.toList()

        if (players.isNotEmpty())
            return@suspendedDatabaseQuery players

        return@suspendedDatabaseQuery Player.find { Players.name like "%$query%" }.toList()
    }
}
