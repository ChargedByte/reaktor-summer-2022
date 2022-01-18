package dev.chargedbyte.reaktor_summer_2022.config

import com.google.inject.AbstractModule
import dev.chargedbyte.reaktor_summer_2022.config.factory.DatabaseFactory
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameServiceImpl
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerServiceImpl
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database

class MainModule(private val application: Application) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)

        val database = DatabaseFactory(application.environment.config).connectAndMigrate()
        bind(Database::class.java).toInstance(database)

        bind(PlayerService::class.java).to(PlayerServiceImpl::class.java).asEagerSingleton()

        bind(GameService::class.java).to(GameServiceImpl::class.java).asEagerSingleton()
    }
}
