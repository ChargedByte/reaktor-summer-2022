package dev.chargedbyte.reaktor_summer_2022.config.factory

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class DatabaseFactory(private val config: AppConfig) {
    fun connectAndMigrate(): Database {
        val pool = createPool()
        val database = Database.connect(pool)
        runMigration(pool)
        return database
    }

    private fun runMigration(dataSource: DataSource) {
        val flyway = Flyway.configure().locations("classpath:dev/chargedbyte/reaktor_summer_2022/db/migration")
            .dataSource(dataSource).load()

        try {
            flyway.migrate()
        } catch (ex: Exception) {
            println("Migration failed: ${ex.message}")
            throw ex
        }
    }

    private fun createPool(): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.custom.database.url
            isAutoCommit = false
            validate()
        }
        return HikariDataSource(hikariConfig)
    }
}
