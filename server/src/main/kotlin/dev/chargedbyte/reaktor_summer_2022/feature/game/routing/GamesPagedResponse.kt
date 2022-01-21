package dev.chargedbyte.reaktor_summer_2022.feature.game.routing

import dev.chargedbyte.reaktor_summer_2022.feature.game.GameDto

data class GamesPagedResponse(val totalPages: Long, val games: List<GameDto>)
