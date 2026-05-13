package com.wholesale.manager.domain.usecase.expense

import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetAllExpensesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<List<Expense>> = repository.getAll()
}
