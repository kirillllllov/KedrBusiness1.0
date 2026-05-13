package com.wholesale.manager.domain.usecase.expense

import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.repository.ExpenseRepository

class GetExpenseByIdUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id: String): Expense? = repository.getById(id)
}
