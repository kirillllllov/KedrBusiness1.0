package com.wholesale.manager.domain.usecase.order

import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.repository.OrderRepository

class UpdateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.update(order)
}
