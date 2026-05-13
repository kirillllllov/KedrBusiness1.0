package com.wholesale.manager.domain.usecase.expense

import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.repository.ExpenseRepository

class CreateExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense) = repository.create(expense)
}
