package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository

class UpdateBatchUseCase(private val repository: BatchRepository) {
    suspend operator fun invoke(batch: Batch) = repository.update(batch)
}
