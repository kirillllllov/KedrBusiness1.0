package com.wholesale.manager.presentation.auth

import androidx.lifecycle.*
import com.wholesale.manager.data.local.AppDatabase
import com.wholesale.manager.di.UserUseCases
import com.wholesale.manager.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class AuthState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(private val useCases: UserUseCases) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    val isLoggedIn: Boolean get() = _state.value.currentUser != null
    val currentRole: String get() = _state.value.currentUser?.role ?: ""

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val hash = AppDatabase.hashPassword(password)
            val user = useCases.getByUsername(username.trim())
            if (user != null && user.passwordHash == hash) {
                _state.update { it.copy(currentUser = user, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, errorMessage = "Неверный логин или пароль") }
            }
        }
    }

    fun logout() {
        _state.update { it.copy(currentUser = null) }
    }
}

class AuthViewModelFactory(private val useCases: UserUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(useCases) as T
    }
}
