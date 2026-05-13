package com.wholesale.manager.domain.repository

import com.wholesale.manager.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAll(): Flow<List<Order>>
    suspend fun getById(id: String): Order?
    suspend fun create(order: Order)
    suspend fun update(order: Order)
    suspend fun delete(id: String)
}
