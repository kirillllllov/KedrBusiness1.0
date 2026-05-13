package com.wholesale.manager.domain.usecase.order

import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.repository.OrderRepository

class GetOrderByIdUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(id: String): Order? = repository.getById(id)
}
