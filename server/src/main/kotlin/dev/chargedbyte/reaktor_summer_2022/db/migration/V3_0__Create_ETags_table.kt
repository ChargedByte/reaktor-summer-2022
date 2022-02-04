package dev.chargedbyte.reaktor_summer_2022.db.migration

import dev.chargedbyte.reaktor_summer_2022.feature.api.data.ETags
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName")
class V3_0__Create_ETags_table : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(ETags)
            ETags.insert { it[id] = "/rps/history" }
        }
    }
}
