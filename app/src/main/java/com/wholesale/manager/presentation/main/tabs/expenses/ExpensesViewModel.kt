package com.wholesale.manager.presentation.main.tabs.expenses

import androidx.lifecycle.*
import com.wholesale.manager.di.ExpenseUseCases
import com.wholesale.manager.domain.model.Expense
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class ExpensesUiState(
    val items: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val typeFilter: String = "",
    val showAddDialog: Boolean = false,
    val editingItem: Expense? = null
) {
    val filtered: List<Expense>
        get() = items.filter { e ->
            (searchQuery.isBlank() || (e.description?.contains(searchQuery, ignoreCase = true) == true) ||
                    e.type.contains(searchQuery, ignoreCase = true)) &&
                    (typeFilter.isBlank() || e.type == typeFilter)
        }

    val totalAmount: Double get() = filtered.sumOf { it.amount }
}

class ExpensesViewModel(private val useCases: ExpenseUseCases) : ViewModel() {
    private val _state = MutableStateFlow(ExpensesUiState())
    val state: StateFlow<ExpensesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list ->
                _state.update { it.copy(items = list) }
            }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onTypeFilterChanged(t: String) = _state.update { it.copy(typeFilter = t) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Expense) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(type: String, amount: Double, date: String, description: String?, batchIds: List<String>) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            if (existing == null) {
                useCases.create(Expense(
                    id = UUID.randomUUID().toString(), lastModified = now,
                    type = type, amount = amount, date = date,
                    description = description, batchIds = batchIds
                ))
            } else {
                useCases.update(existing.copy(
                    type = type, amount = amount, date = date,
                    description = description, batchIds = batchIds, lastModified = now
                ))
            }
            dismissDialog()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { useCases.delete(id) }
    }
}

class ExpensesViewModelFactory(private val useCases: ExpenseUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpensesViewModel(useCases) as T
    }
}
