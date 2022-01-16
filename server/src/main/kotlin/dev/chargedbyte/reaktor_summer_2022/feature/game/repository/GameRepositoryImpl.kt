package dev.chargedbyte.reaktor_summer_2022.feature.game.repository

import dev.chargedbyte.reaktor_summer_2022.feature.game.Game
import org.litote.kmongo.coroutine.CoroutineDatabase
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(database: CoroutineDatabase) : GameRepository {
    private val col = database.getCollection<Game>()

    override suspend fun findAll() = col.find().toList()

    override suspend fun findById(id: String) = col.findOneById(id)

    override suspend fun create(entity: Game) = col.insertOne(entity).wasAcknowledged()

    override suspend fun update(entity: Game) = col.replaceOneById(entity.id!!, entity).wasAcknowledged()

    override suspend fun deleteById(id: String) = col.deleteOneById(id).wasAcknowledged()

    override suspend fun count() = col.countDocuments()

    override suspend fun existsById(id: String) = findById(id) != null
}
