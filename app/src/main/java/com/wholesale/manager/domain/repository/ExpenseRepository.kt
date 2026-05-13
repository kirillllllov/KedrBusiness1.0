package com.wholesale.manager.domain.repository

import com.wholesale.manager.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAll(): Flow<List<Expense>>
    suspend fun getById(id: String): Expense?
    suspend fun create(expense: Expense)
    suspend fun update(expense: Expense)
    suspend fun delete(id: String)
}
