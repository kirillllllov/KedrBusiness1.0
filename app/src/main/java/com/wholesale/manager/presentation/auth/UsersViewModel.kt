package com.wholesale.manager.presentation.auth

import androidx.lifecycle.*
import com.wholesale.manager.data.local.AppDatabase
import com.wholesale.manager.di.UserUseCases
import com.wholesale.manager.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class UsersUiState(
    val users: List<User> = emptyList(),
    val showDialog: Boolean = false,
    val editingUser: User? = null,
    val errorMessage: String? = null
)

class UsersViewModel(private val useCases: UserUseCases) : ViewModel() {

    private val _state = MutableStateFlow(UsersUiState())
    val state: StateFlow<UsersUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list -> _state.update { it.copy(users = list) } }
        }
    }

    fun showAddDialog() = _state.update { it.copy(showDialog = true, editingUser = null, errorMessage = null) }
    fun showEditDialog(user: User) = _state.update { it.copy(showDialog = true, editingUser = user, errorMessage = null) }
    fun dismissDialog() = _state.update { it.copy(showDialog = false, editingUser = null, errorMessage = null) }

    fun save(displayName: String, username: String, password: String, role: String) {
        viewModelScope.launch {
            val existing = _state.value.editingUser
            if (existing == null) {
                val hash = AppDatabase.hashPassword(password)
                useCases.create(
                    User(
                        id = UUID.randomUUID().toString(),
                        username = username.trim(),
                        passwordHash = hash,
                        role = role,
                        displayName = displayName.trim()
                    )
                )
            } else {
                val hash = if (password.isNotBlank()) AppDatabase.hashPassword(password) else existing.passwordHash
                useCases.update(
                    existing.copy(
                        displayName = displayName.trim(),
                        username = username.trim(),
                        passwordHash = hash,
                        role = role
                    )
                )
            }
            dismissDialog()
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            if (user.role == User.ROLE_DIRECTOR && useCases.countDirectors() <= 1) {
                _state.update { it.copy(errorMessage = "Нельзя удалить последнего директора") }
                return@launch
            }
            useCases.delete(user.id)
        }
    }
}

class UsersViewModelFactory(private val useCases: UserUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UsersViewModel(useCases) as T
    }
}
