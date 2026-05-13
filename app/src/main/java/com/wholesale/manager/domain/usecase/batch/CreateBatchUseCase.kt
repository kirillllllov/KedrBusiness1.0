package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository

class CreateBatchUseCase(private val repository: BatchRepository) {
    suspend operator fun invoke(batch: Batch) = repository.create(batch)
}
