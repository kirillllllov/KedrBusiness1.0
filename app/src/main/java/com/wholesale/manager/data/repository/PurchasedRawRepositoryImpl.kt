package com.wholesale.manager.data.repository

import com.wholesale.manager.data.local.dao.PurchasedRawDao
import com.wholesale.manager.data.local.entity.PurchasedRawEntity
import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.domain.repository.PurchasedRawRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class PurchasedRawRepositoryImpl(private val dao: PurchasedRawDao) : PurchasedRawRepository {

    override fun getAll(): Flow<List<PurchasedRaw>> =
        dao.getAllNotDeleted().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): PurchasedRaw? = dao.getById(id)?.toDomain()

    override suspend fun create(purchase: PurchasedRaw) = dao.insert(purchase.toEntity())

    override suspend fun update(purchase: PurchasedRaw) = dao.update(purchase.toEntity())

    override suspend fun delete(id: String) {
        dao.softDelete(id, Instant.now().toString())
    }

    private fun PurchasedRawEntity.toDomain() = PurchasedRaw(
        id = id, number = number, serverId = serverId, lastModified = lastModified,
        isDeleted = isDeleted, type = type, quantityKg = quantityKg,
        purchasePriceTotal = purchasePriceTotal, pricePerKg = pricePerKg,
        supplierName = supplierName, purchaseDate = purchaseDate, status = status, batchId = batchId
    )

    private fun PurchasedRaw.toEntity() = PurchasedRawEntity(
        id = id, number = number, serverId = serverId, lastModified = lastModified,
        isDeleted = isDeleted, type = type, quantityKg = quantityKg,
        purchasePriceTotal = purchasePriceTotal, pricePerKg = pricePerKg,
        supplierName = supplierName, purchaseDate = purchaseDate, status = status, batchId = batchId
    )
}
