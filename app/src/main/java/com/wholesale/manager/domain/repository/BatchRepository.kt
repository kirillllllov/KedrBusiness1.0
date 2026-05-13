package com.wholesale.manager.domain.repository

import com.wholesale.manager.domain.model.Batch
import kotlinx.coroutines.flow.Flow

interface BatchRepository {
    fun getAll(): Flow<List<Batch>>
    suspend fun getById(id: String): Batch?
    suspend fun create(batch: Batch)
    suspend fun update(batch: Batch)
    suspend fun delete(id: String)
}
