package dev.chargedbyte.reaktor_summer_2022.config

import com.google.inject.AbstractModule
import com.mongodb.ConnectionString
import dev.chargedbyte.reaktor_summer_2022.feature.game.repository.GameRepository
import dev.chargedbyte.reaktor_summer_2022.feature.game.repository.GameRepositoryImpl
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameService
import dev.chargedbyte.reaktor_summer_2022.feature.game.service.GameServiceImpl
import dev.chargedbyte.reaktor_summer_2022.feature.player.repository.PlayerRepository
import dev.chargedbyte.reaktor_summer_2022.feature.player.repository.PlayerRepositoryImpl
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerService
import dev.chargedbyte.reaktor_summer_2022.feature.player.service.PlayerServiceImpl
import io.ktor.application.*
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MainModule(private val application: Application) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)

        val mongoPair = getMongoClient()
        bind(CoroutineClient::class.java).toInstance(mongoPair.first)
        bind(CoroutineDatabase::class.java).toInstance(mongoPair.second)

        bind(PlayerService::class.java).to(PlayerServiceImpl::class.java).asEagerSingleton()
        bind(PlayerRepository::class.java).to(PlayerRepositoryImpl::class.java).asEagerSingleton()

        bind(GameService::class.java).to(GameServiceImpl::class.java).asEagerSingleton()
        bind(GameRepository::class.java).to(GameRepositoryImpl::class.java).asEagerSingleton()
    }

    private fun getMongoClient(): Pair<CoroutineClient, CoroutineDatabase> {
        val config = application.environment.config

        val url = config.property("reaktor_summer_2022.database.url").getString()
        val mongoUrl = ConnectionString(url)

        val databaseName = mongoUrl.database ?: "reaktor_summer_2022"

        val client = KMongo.createClient(mongoUrl).coroutine
        val database = client.getDatabase(databaseName)

        return Pair(client, database)
    }
}
