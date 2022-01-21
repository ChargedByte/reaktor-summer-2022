package dev.chargedbyte.reaktor_summer_2022.feature.game.service

import dev.chargedbyte.reaktor_summer_2022.feature.api.model.ApiGame
import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.utils.databaseQuery
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private val playerService: PlayerService) : GameService {
    override suspend fun saveAll(games: List<ApiGame>) {
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
            }
        }
    }

    override suspend fun findById(id: String) = databaseQuery { Game.findById(id) }

    override suspend fun existsById(id: String) = findById(id) != null
}
