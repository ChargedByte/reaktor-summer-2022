package dev.chargedbyte.reaktor_summer_2022.config

import com.google.inject.AbstractModule
import com.mongodb.ConnectionString
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
