package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository

class GetBatchByIdUseCase(private val repository: BatchRepository) {
    suspend operator fun invoke(id: String): Batch? = repository.getById(id)
}
