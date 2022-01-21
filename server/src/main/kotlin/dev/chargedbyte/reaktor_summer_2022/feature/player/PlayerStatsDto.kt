package dev.chargedbyte.reaktor_summer_2022.feature.player

import dev.chargedbyte.reaktor_summer_2022.model.Hand

data class PlayerStatsDto(val wins: Long, val total: Long, val winRatio: Double, val mostPlayed: Hand)
