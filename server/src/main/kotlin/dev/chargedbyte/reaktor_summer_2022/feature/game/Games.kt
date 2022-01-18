package dev.chargedbyte.reaktor_summer_2022.feature.game

import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Games : IdTable<String>() {
    override val id = text("id").entityId()
    val playedAt = datetime("played_at")
    val playerA = reference("player_a", Players)
    val handA = enumerationByName("hand_a", 8, Hand::class)
    val playerB = reference("player_b", Players)
    val handB = enumerationByName("hand_b", 8, Hand::class)
    val winner = optReference("winner", Players)
    override val primaryKey = PrimaryKey(id)
}
