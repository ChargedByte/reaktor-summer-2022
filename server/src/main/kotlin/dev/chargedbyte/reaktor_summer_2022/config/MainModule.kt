package dev.chargedbyte.reaktor_summer_2022.config

import com.google.inject.AbstractModule
import dev.chargedbyte.reaktor_summer_2022.config.factory.DatabaseFactory
import dev.chargedbyte.reaktor_summer_2022.feature.api.HistoryLoader
import dev.chargedbyte.reaktor_summer_2022.feature.game.routing.GameRoutes
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameServiceImpl
import dev.chargedbyte.reaktor_summer_2022.feature.player.routing.PlayerRoutes
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerServiceImpl
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import org.jetbrains.exposed.sql.Database

class MainModule(private val application: Application) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)

        val database = DatabaseFactory(application.environment.config).connectAndMigrate()
        bind(Database::class.java).toInstance(database)

        val client = HttpClient(CIO) {
            install(UserAgent) {
                agent = "reaktor-summer-2022/0.0.1"
            }
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
        bind(HttpClient::class.java).toInstance(client)

        bind(HistoryLoader::class.java).asEagerSingleton()

        bind(PlayerRoutes::class.java).asEagerSingleton()
        bind(PlayerService::class.java).to(PlayerServiceImpl::class.java).asEagerSingleton()

        bind(GameRoutes::class.java).asEagerSingleton()
        bind(GameService::class.java).to(GameServiceImpl::class.java).asEagerSingleton()
    }
}
