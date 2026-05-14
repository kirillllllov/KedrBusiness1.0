package com.wholesale.manager.domain.repository

import com.wholesale.manager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAll(): Flow<List<User>>
    suspend fun getById(id: String): User?
    suspend fun getByUsername(username: String): User?
    suspend fun create(user: User)
    suspend fun update(user: User)
    suspend fun delete(id: String)
    suspend fun countDirectors(): Int
}
