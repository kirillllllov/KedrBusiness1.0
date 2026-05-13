package com.wholesale.manager.domain.usecase.purchase

import com.wholesale.manager.domain.repository.PurchasedRawRepository

class DeletePurchaseUseCase(private val repository: PurchasedRawRepository) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
