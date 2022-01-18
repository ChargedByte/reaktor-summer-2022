package dev.chargedbyte.reaktor_summer_2022.db.migration

import dev.chargedbyte.reaktor_summer_2022.feature.player.Players
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName")
class V1_0__Create_Players_table : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(Players)
        }
    }
}
