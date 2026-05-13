package com.wholesale.manager.domain.usecase.purchase

import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.domain.repository.PurchasedRawRepository

class CreatePurchaseUseCase(private val repository: PurchasedRawRepository) {
    suspend operator fun invoke(purchase: PurchasedRaw) = repository.create(purchase)
}
