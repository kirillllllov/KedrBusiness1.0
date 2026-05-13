package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository
import kotlinx.coroutines.flow.Flow

class GetAllBatchesUseCase(private val repository: BatchRepository) {
    operator fun invoke(): Flow<List<Batch>> = repository.getAll()
}
