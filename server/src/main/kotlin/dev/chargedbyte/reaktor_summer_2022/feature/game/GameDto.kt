package dev.chargedbyte.reaktor_summer_2022.feature.game

import dev.chargedbyte.reaktor_summer_2022.feature.player.PlayerDto
import dev.chargedbyte.reaktor_summer_2022.model.Hand
import java.time.LocalDateTime

data class GameDto(
    val id: String?,
    val playedAt: LocalDateTime,
    val playerA: PlayerDto,
    val handA: Hand,
    val playerB: PlayerDto,
    val handB: Hand,
    val winner: PlayerDto?
)
