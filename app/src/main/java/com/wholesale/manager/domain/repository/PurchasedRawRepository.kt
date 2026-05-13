package com.wholesale.manager.domain.repository

import com.wholesale.manager.domain.model.PurchasedRaw
import kotlinx.coroutines.flow.Flow

interface PurchasedRawRepository {
    fun getAll(): Flow<List<PurchasedRaw>>
    suspend fun getById(id: String): PurchasedRaw?
    suspend fun create(purchase: PurchasedRaw)
    suspend fun update(purchase: PurchasedRaw)
    suspend fun delete(id: String)
}
