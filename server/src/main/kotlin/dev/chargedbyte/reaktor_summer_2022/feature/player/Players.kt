package dev.chargedbyte.reaktor_summer_2022.feature.player

import org.jetbrains.exposed.dao.id.IdTable

object Players : IdTable<Long>() {
    override val id = long("id").entityId()
    val name = text("name").uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}
