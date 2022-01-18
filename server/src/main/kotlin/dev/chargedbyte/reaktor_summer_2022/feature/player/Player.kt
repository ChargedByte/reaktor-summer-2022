package dev.chargedbyte.reaktor_summer_2022.feature.player

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Player(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, Player>(Players)

    var name by Players.name

    fun toDto() = PlayerDto(id.value, name)
}
