package dev.chargedbyte.reaktor_summer_2022.feature.game

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.LocalDateTime

data class Game(
    @BsonId val id: String? = null,
    val time: LocalDateTime,
    val playerA: Id<Player>,
    val handA: Hand,
    val playerB: Id<Player>,
    val handB: Hand,
    val winner: Id<Player>? = null
)
