package com.wholesale.manager.domain.usecase.order

import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.repository.OrderRepository

class CreateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.create(order)
}
