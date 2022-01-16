package dev.chargedbyte.reaktor_summer_2022.feature.player.repository

import dev.chargedbyte.reaktor_summer_2022.feature.player.Player
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(database: CoroutineDatabase) : PlayerRepository {
    private val col = database.getCollection<Player>()

    override suspend fun findAll() = col.find().toList()

    override suspend fun findById(id: Id<Player>) = col.findOneById(id)

    override suspend fun create(entity: Player) = col.insertOne(entity).wasAcknowledged()

    override suspend fun update(entity: Player) = col.replaceOneById(entity.id, entity).wasAcknowledged()

    override suspend fun deleteById(id: Id<Player>) = col.deleteOneById(id).wasAcknowledged()

    override suspend fun count() = col.countDocuments()

    override suspend fun existsById(id: Id<Player>) = findById(id) != null
}
