package com.wholesale.manager.presentation.main.tabs.expenses

import androidx.lifecycle.*
import com.wholesale.manager.di.ExpenseUseCases
import com.wholesale.manager.di.PurchaseUseCases
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.model.PurchasedRaw
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class ExpensesUiState(
    val items: List<Expense> = emptyList(),
    val purchases: List<PurchasedRaw> = emptyList(),
    val searchQuery: String = "",
    val typeFilter: String = "",
    val sortNewest: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingItem: Expense? = null
) {
    val filtered: List<Expense>
        get() {
            val base = items.filter { e ->
                (searchQuery.isBlank() || (e.description?.contains(searchQuery, ignoreCase = true) == true) ||
                        e.type.contains(searchQuery, ignoreCase = true)) &&
                        (typeFilter.isBlank() || e.type == typeFilter)
            }
            return if (sortNewest) base.sortedByDescending { it.date }
            else base.sortedBy { it.date }
        }

    val totalAmount: Double get() = filtered.sumOf { it.amount }
}

class ExpensesViewModel(
    private val useCases: ExpenseUseCases,
    private val purchaseUseCases: PurchaseUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(ExpensesUiState())
    val state: StateFlow<ExpensesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list -> _state.update { it.copy(items = list) } }
        }
        viewModelScope.launch {
            purchaseUseCases.getAll().collect { list -> _state.update { it.copy(purchases = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onTypeFilterChanged(t: String) = _state.update { it.copy(typeFilter = t) }
    fun toggleSort() = _state.update { it.copy(sortNewest = !it.sortNewest) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Expense) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(type: String, amount: Double, date: String, description: String?, purchaseId: String?) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            if (existing == null) {
                useCases.create(
                    Expense(
                        id = UUID.randomUUID().toString(), lastModified = now,
                        type = type, amount = amount, date = date,
                        description = description, purchaseId = purchaseId
                    )
                )
            } else {
                useCases.update(
                    existing.copy(
                        type = type, amount = amount, date = date,
                        description = description, purchaseId = purchaseId, lastModified = now
                    )
                )
            }
            dismissDialog()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { useCases.delete(id) }
    }
}

class ExpensesViewModelFactory(
    private val useCases: ExpenseUseCases,
    private val purchaseUseCases: PurchaseUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpensesViewModel(useCases, purchaseUseCases) as T
    }
}
