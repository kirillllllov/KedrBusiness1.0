package com.wholesale.manager.domain.usecase.order

import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetAllOrdersUseCase(private val repository: OrderRepository) {
    operator fun invoke(): Flow<List<Order>> = repository.getAll()
}
