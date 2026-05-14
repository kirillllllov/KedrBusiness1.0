package com.wholesale.manager.domain.usecase.user

import com.wholesale.manager.domain.model.User
import com.wholesale.manager.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase(private val repo: UserRepository) {
    operator fun invoke(): Flow<List<User>> = repo.getAll()
}

class GetUserByUsernameUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(username: String): User? = repo.getByUsername(username)
}

class CreateUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(user: User) = repo.create(user)
}

class UpdateUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(user: User) = repo.update(user)
}

class DeleteUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(id: String) = repo.delete(id)
}

class CountDirectorsUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(): Int = repo.countDirectors()
}
