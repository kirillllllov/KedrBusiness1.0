package com.wholesale.manager.domain.usecase.expense

import com.wholesale.manager.domain.repository.ExpenseRepository

class DeleteExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
