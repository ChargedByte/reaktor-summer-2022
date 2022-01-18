package dev.chargedbyte.reaktor_summer_2022.feature.player

import org.jetbrains.exposed.dao.id.IntIdTable

object Players : IntIdTable() {
    val name = text("name").uniqueIndex()
}
