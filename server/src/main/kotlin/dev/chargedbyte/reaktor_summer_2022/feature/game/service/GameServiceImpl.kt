package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.feature.game.Games
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.model.Hand
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

val saveAllMutex = Mutex()

class GameServiceImpl @Inject constructor(private val playerService: PlayerService) : GameService {
    private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)

    override suspend fun findAllGameIds() =
        newSuspendedTransaction { Games.slice(Games.id).selectAll().map { it[Games.id].value } }

    override suspend fun findAllPaged(size: Int, page: Long): Pair<List<Game>, Long> {
        val totalPages = (count() / size) + 1

        val pA = Players.alias("pA")
        val pB = Players.alias("pB")
        val w = Players.alias("w")

        val games = newSuspendedTransaction {
            Games.innerJoin(pA, { playerA }, { pA[Players.id] }).innerJoin(pB, { Games.playerB }, { pB[Players.id] })
                .innerJoin(w, { Games.winner }, { w[Players.id] }).selectAll().limit(size, size * page)
                .orderBy(Games.playedAt).map { Game.wrapRow(it) }.toList()
        }

        return Pair(games, totalPages)
    }

    override suspend fun findGamesByPlayerIdPaged(playerId: Int, size: Int, page: Long): Pair<List<Game>, Long> {
        val totalPages = (countGamesByPlayerId(playerId) / size) + 1

        val pA = Players.alias("pA")
        val pB = Players.alias("pB")
        val w = Players.alias("w")

        val games = newSuspendedTransaction {
            Games.innerJoin(pA, { playerA }, { pA[Players.id] }).innerJoin(pB, { Games.playerB }, { pB[Players.id] })
                .innerJoin(w, { Games.winner }, { w[Players.id] })
                .select { (Games.playerA eq playerId) or (Games.playerB eq playerId) }.limit(size, size * page)
                .orderBy(Games.playedAt).map { Game.wrapRow(it) }.toList()
        }

        return Pair(games, totalPages)
    }

    override suspend fun saveAll(cursor: String, fetchDuration: Duration, games: List<ApiGame>) =
        saveAllMutex.withLock {
            val gameIds = findAllGameIds()

            val processGames = games.filter { it.gameId !in gameIds }

            val startTime = Instant.now()

            val players = processGames.flatMap { listOf(it.playerA.name, it.playerB.name) }.distinct()
                .map { playerService.findByNameOrCreate(it) }

            newSuspendedTransaction {
                Games.batchInsert(processGames) { game ->
                    val playerA = players.first { it.name == game.playerA.name }
                    val playerB = players.first { it.name == game.playerB.name }

                    val handA = game.playerA.played
                    val handB = game.playerB.played

                    val winner = if (handA.beats(handB)) playerA else if (handB.beats(handA)) playerB else null

                    this[Games.id] = game.gameId
                    this[Games.playedAt] = LocalDateTime.ofInstant(Instant.ofEpochMilli(game.t), ZoneId.systemDefault())
                    this[Games.playerA] = playerA.id
                    this[Games.handA] = handA
                    this[Games.playerB] = playerB.id
                    this[Games.handB] = handB
                    this[Games.winner] = winner?.id
                }
            }

            val duration = Duration.between(startTime, Instant.now())

            logger.info("Saved ${processGames.size} new games from cursor $cursor in ${fetchDuration + duration}")
        }

    override suspend fun findById(id: String) = newSuspendedTransaction { Game.findById(id) }

    override suspend fun existsById(id: String) = findById(id) != null

    override suspend fun count() = newSuspendedTransaction { Game.count() }

    override suspend fun countGamesByPlayerId(playerId: Int) =
        newSuspendedTransaction { Games.select { (Games.playerA eq playerId) or (Games.playerB eq playerId) }.count() }

    override suspend fun countGamesByPlayerIdAndWon(playerId: Int) =
        newSuspendedTransaction { Games.select { Games.winner eq playerId }.count() }

    override suspend fun countGamesByPlayerIdWhereHandWasPlayed(playerId: Int, hand: Hand) = newSuspendedTransaction {
        Games.select { ((Games.playerA eq playerId) and (Games.handA eq hand)) or ((Games.playerB eq playerId) and (Games.handB eq hand)) }
            .count()
    }

    override suspend fun mostPlayedHandByPlayerId(playerId: Int) =
        Hand.values().map { it to countGamesByPlayerIdWhereHandWasPlayed(playerId, it) }
            .maxByOrNull { it.second }!!.first
}
