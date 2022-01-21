package dev.chargedbyte.reaktor_summer_2022.feature.api.model

data class ApiGame(val type: GameType, val gameId: String, val t: Long, val playerA: ApiPlayer, val playerB: ApiPlayer)
