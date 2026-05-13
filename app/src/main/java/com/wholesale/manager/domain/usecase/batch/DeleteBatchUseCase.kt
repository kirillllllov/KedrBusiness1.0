package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.repository.BatchRepository

class DeleteBatchUseCase(private val repository: BatchRepository) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
