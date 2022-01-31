package dev.chargedbyte.reaktor_summer_2022.feature.game

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.ZoneOffset

class Game(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Game>(Games)

    var playedAt by Games.playedAt
    var playerA by Player referencedOn Games.playerA
    var handA by Games.handA
    var playerB by Player referencedOn Games.playerB
    var handB by Games.handB
    var winner by Player optionalReferencedOn Games.winner

    fun toDto() = GameDto(
        id.value,
        playedAt.toEpochSecond(ZoneOffset.UTC),
        playerA.toDto(),
        handA,
        playerB.toDto(),
        handB,
        winner?.toDto()
    )
}
