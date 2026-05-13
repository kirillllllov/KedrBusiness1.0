package com.wholesale.manager.domain.usecase.order

import com.wholesale.manager.domain.repository.OrderRepository

class DeleteOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
