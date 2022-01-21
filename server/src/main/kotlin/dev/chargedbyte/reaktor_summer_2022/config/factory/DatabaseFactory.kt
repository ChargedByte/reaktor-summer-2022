package dev.chargedbyte.reaktor_summer_2022.config.factory

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class DatabaseFactory(config: ApplicationConfig) {
    private val databaseUrl = config.property("reaktor_summer_2022.database.url").getString()

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
            flyway.info()
            flyway.migrate()
        } catch (ex: Exception) {
            println("Migration failed: ${ex.message}")
            throw ex
        }
    }

    private fun createPool(): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = databaseUrl
            isAutoCommit = false
            validate()
        }
        return HikariDataSource(hikariConfig)
    }
}
