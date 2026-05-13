package com.wholesale.manager.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wholesale.manager.data.local.dao.ExpenseDao
import com.wholesale.manager.data.local.entity.ExpenseEntity
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class ExpenseRepositoryImpl(private val dao: ExpenseDao) : ExpenseRepository {

    private val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type

    override fun getAll(): Flow<List<Expense>> =
        dao.getAllNotDeleted().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): Expense? = dao.getById(id)?.toDomain()

    override suspend fun create(expense: Expense) = dao.insert(expense.toEntity())

    override suspend fun update(expense: Expense) = dao.update(expense.toEntity())

    override suspend fun delete(id: String) {
        dao.softDelete(id, Instant.now().toString())
    }

    private fun ExpenseEntity.toDomain(): Expense {
        val batchIds: List<String> = gson.fromJson(batchIdsJson, listType) ?: emptyList()
        return Expense(
            id = id, serverId = serverId, lastModified = lastModified, isDeleted = isDeleted,
            type = type, amount = amount, date = date, description = description,
            batchIds = batchIds
        )
    }

    private fun Expense.toEntity() = ExpenseEntity(
        id = id, serverId = serverId, lastModified = lastModified, isDeleted = isDeleted,
        type = type, amount = amount, date = date, description = description,
        batchIdsJson = gson.toJson(batchIds)
    )
}
