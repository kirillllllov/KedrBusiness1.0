package com.wholesale.manager.domain.usecase.purchase

import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.domain.repository.PurchasedRawRepository
import kotlinx.coroutines.flow.Flow

class GetAllPurchasesUseCase(private val repository: PurchasedRawRepository) {
    operator fun invoke(): Flow<List<PurchasedRaw>> = repository.getAll()
}
