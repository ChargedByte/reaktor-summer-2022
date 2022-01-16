package dev.chargedbyte.reaktor_summer_2022.core.repository

interface CrudRepository<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: ID): T?
    suspend fun create(entity: T): Boolean
    suspend fun update(entity: T): Boolean
    suspend fun deleteById(id: ID): Boolean
    suspend fun count(): Long
    suspend fun existsById(id: ID): Boolean
}
