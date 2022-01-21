package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.feature.game.Games
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private val playerService: PlayerService) : GameService {
    private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)

    override suspend fun findAllPagedAsync(size: Int, page: Long): Deferred<Pair<List<Game>, Long>> =
        suspendedTransactionAsync {
            val totalPages = (count() / size) + 1

            val pA = Players.alias("pA")
            val pB = Players.alias("pB")
            val w = Players.alias("w")

            val games = Games.innerJoin(pA, { playerA }, { pA[Players.id] })
                .innerJoin(pB, { Games.playerB }, { pB[Players.id] }).innerJoin(w, { Games.winner }, { w[Players.id] })
                .selectAll().limit(size, size * page).orderBy(Games.playedAt).map { Game.wrapRow(it) }.toList()

            return@suspendedTransactionAsync Pair(games, totalPages)
        }

    override suspend fun saveAllAsync(games: List<ApiGame>) = suspendedTransactionAsync {
        var count = 0

        games.forEach {
            if (!existsById(it.gameId)) {
                val playerA = playerService.findByNameOrCreateAsync(it.playerA.name).await()
                val playerB = playerService.findByNameOrCreateAsync(it.playerB.name).await()

                val handA = it.playerA.played
                val handB = it.playerB.played

                val winner = if (handA.beats(handB)) playerA else if (handB.beats(handA)) playerB else null

                val playedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.t), ZoneId.systemDefault())

                Game.new(it.gameId) {
                    this.playedAt = playedAt
                    this.playerA = playerA
                    this.handA = handA
                    this.playerB = playerB
                    this.handB = handB
                    this.winner = winner
                }

                count++
            }
        }

        logger.info("Created new games: $count")
    }

    override suspend fun findByIdAsync(id: String) = suspendedTransactionAsync { Game.findById(id) }

    override suspend fun existsById(id: String) = findByIdAsync(id).await() != null

    override suspend fun count() = newSuspendedTransaction { Game.count() }

    override suspend fun findGamesByPlayerIdAsync(playerId: Int) = suspendedTransactionAsync {
        Game.find { (Games.playerA eq playerId) or (Games.playerB eq playerId) }.toList()
    }

    override suspend fun findGamesWonByPlayerIdAsync(playerId: Int) = suspendedTransactionAsync {
        Game.find { Games.winner eq playerId }.toList()
    }
}
