package com.wholesale.manager.data.repository

import com.wholesale.manager.data.local.dao.BatchDao
import com.wholesale.manager.data.local.entity.BatchEntity
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class BatchRepositoryImpl(private val dao: BatchDao) : BatchRepository {

    override fun getAll(): Flow<List<Batch>> =
        dao.getAllNotDeleted().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): Batch? = dao.getById(id)?.toDomain()

    override suspend fun create(batch: Batch) = dao.insert(batch.toEntity())

    override suspend fun update(batch: Batch) = dao.update(batch.toEntity())

    override suspend fun delete(id: String) {
        dao.softDelete(id, Instant.now().toString())
    }

    private fun BatchEntity.toDomain() = Batch(
        id = id, serverId = serverId, number = number,
        formationDate = formationDate, rawQuantityKg = rawQuantityKg,
        outputPercent = outputPercent, costPrice = costPrice,
        optimalPricePerKg = optimalPricePerKg, status = status,
        lastModified = lastModified, isDeleted = isDeleted
    )

    private fun Batch.toEntity() = BatchEntity(
        id = id, serverId = serverId, number = number,
        formationDate = formationDate, rawQuantityKg = rawQuantityKg,
        outputPercent = outputPercent, costPrice = costPrice,
        optimalPricePerKg = optimalPricePerKg, status = status,
        lastModified = lastModified, isDeleted = isDeleted
    )
}
