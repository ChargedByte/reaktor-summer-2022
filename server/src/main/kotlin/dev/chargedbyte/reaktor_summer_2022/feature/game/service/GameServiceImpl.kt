package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.feature.game.Games
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.model.Hand
import dev.chargedbyte.reaktor_summer_2022.utils.suspendedDatabaseQuery
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private val playerService: PlayerService) : GameService {
    private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)

    override suspend fun findAllPaged(size: Int, page: Long): Pair<List<Game>, Long> = suspendedDatabaseQuery {
        val totalPages = (count() / size) + 1

        val pA = Players.alias("pA")
        val pB = Players.alias("pB")
        val w = Players.alias("w")

        val games =
            Games.innerJoin(pA, { playerA }, { pA[Players.id] }).innerJoin(pB, { Games.playerB }, { pB[Players.id] })
                .innerJoin(w, { Games.winner }, { w[Players.id] }).selectAll().limit(size, size * page)
                .orderBy(Games.playedAt).map { Game.wrapRow(it) }.toList()

        Pair(games, totalPages)
    }

    override suspend fun saveAll(cursor: String, fetchDuration: Duration, games: List<ApiGame>) =
        suspendedDatabaseQuery {
            var count = 0

            val startTime = Instant.now()

            games.forEach {
                if (!existsById(it.gameId)) {
                    val playerA = playerService.findByNameOrCreate(it.playerA.name)
                    val playerB = playerService.findByNameOrCreate(it.playerB.name)

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

            val duration = Duration.between(startTime, Instant.now())

            logger.debug("Created $count new games for cursor $cursor in $duration")

            logger.info("Added $count new games from cursor $cursor in ${fetchDuration + duration}")
        }

    override suspend fun findById(id: String) = suspendedDatabaseQuery { Game.findById(id) }

    override suspend fun existsById(id: String) = findById(id) != null

    override suspend fun count() = suspendedDatabaseQuery { Game.count() }

    override suspend fun countGamesByPlayerId(playerId: Int) =
        suspendedDatabaseQuery { Games.select { (Games.playerA eq playerId) or (Games.playerB eq playerId) }.count() }

    override suspend fun countGamesByPlayerIdAndWon(playerId: Int) =
        suspendedDatabaseQuery { Games.select { Games.winner eq playerId }.count() }

    override suspend fun countGamesByPlayerIdWhereHandWasPlayed(playerId: Int, hand: Hand) = suspendedDatabaseQuery {
        Games.select { ((Games.playerA eq playerId) and (Games.handA eq hand)) or ((Games.playerB eq playerId) and (Games.handB eq hand)) }
            .count()
    }

    override suspend fun mostPlayedHandByPlayerId(playerId: Int) =
        Hand.values().map { it to countGamesByPlayerIdWhereHandWasPlayed(playerId, it) }
            .maxByOrNull { it.second }!!.first
}
