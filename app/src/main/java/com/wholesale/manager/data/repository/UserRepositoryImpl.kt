package com.wholesale.manager.data.repository

import com.wholesale.manager.data.local.dao.UserDao
import com.wholesale.manager.data.local.entity.UserEntity
import com.wholesale.manager.domain.model.User
import com.wholesale.manager.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {

    override fun getAll(): Flow<List<User>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): User? = dao.getById(id)?.toDomain()

    override suspend fun getByUsername(username: String): User? =
        dao.getByUsername(username)?.toDomain()

    override suspend fun create(user: User) = dao.insert(user.toEntity())

    override suspend fun update(user: User) = dao.update(user.toEntity())

    override suspend fun delete(id: String) = dao.delete(id)

    override suspend fun countDirectors(): Int = dao.countByRole(User.ROLE_DIRECTOR)

    private fun UserEntity.toDomain() = User(
        id = id, username = username, passwordHash = passwordHash,
        role = role, displayName = displayName
    )

    private fun User.toEntity() = UserEntity(
        id = id, username = username, passwordHash = passwordHash,
        role = role, displayName = displayName
    )
}
