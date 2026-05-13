package com.wholesale.manager.domain.usecase.purchase

import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.domain.repository.PurchasedRawRepository

class GetPurchaseByIdUseCase(private val repository: PurchasedRawRepository) {
    suspend operator fun invoke(id: String): PurchasedRaw? = repository.getById(id)
}
