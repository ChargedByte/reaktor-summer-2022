package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.feature.game.Games
import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.utils.databaseQuery
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private val playerService: PlayerService) : GameService {
    private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)

    override suspend fun findAllPaged(size: Int, page: Long): Pair<List<Game>, Long> {
        val totalPages = (count() / size) + 1

        val games = databaseQuery {
            val pA = Players.alias("pA")
            val pB = Players.alias("pB")
            val w = Players.alias("w")

            Games.innerJoin(pA, { playerA }, { pA[Players.id] }).innerJoin(pB, { Games.playerB }, { pB[Players.id] })
                .innerJoin(w, { Games.winner }, { w[Players.id] }).selectAll().limit(size, size * page)
                .orderBy(Games.playedAt).map { Game.wrapRow(it) }.toList()
        }

        return Pair(games, totalPages)
    }

    override suspend fun saveAll(games: List<ApiGame>) {
        var count = 0

        games.forEach {
            if (!existsById(it.gameId)) {
                val playerA = playerService.findByNameOrCreate(it.playerA.name)
                val playerB = playerService.findByNameOrCreate(it.playerB.name)

                val handA = it.playerA.played
                val handB = it.playerB.played

                val winner = if (handA.beats(handB)) playerA else if (handB.beats(handA)) playerB else null

                val playedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.t), ZoneId.systemDefault())

                databaseQuery {
                    Game.new(it.gameId) {
                        this.playedAt = playedAt
                        this.playerA = playerA
                        this.handA = handA
                        this.playerB = playerB
                        this.handB = handB
                        this.winner = winner
                    }
                }

                count++
            }
        }

        logger.info("Created new games: $count")
    }

    override suspend fun findById(id: String) = databaseQuery { Game.findById(id) }

    override suspend fun existsById(id: String) = findById(id) != null

    override suspend fun count() = databaseQuery { Game.count() }
}
