package dev.chargedbyte.reaktor_summer_2022.feature.api.data

import org.jetbrains.exposed.dao.id.IdTable

object ETags : IdTable<String>() {
    override val id = varchar("cursor", 255).entityId()
    val etag = varchar("etag", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}
