package com.wholesale.manager.presentation.main.tabs.expenses

import androidx.lifecycle.*
import com.wholesale.manager.di.BatchUseCases
import com.wholesale.manager.di.ExpenseUseCases
import com.wholesale.manager.di.PurchaseUseCases
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.model.PurchasedRaw
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class ExpensesUiState(
    val items: List<Expense> = emptyList(),
    val purchases: List<PurchasedRaw> = emptyList(),
    val batches: List<Batch> = emptyList(),
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
    private val purchaseUseCases: PurchaseUseCases,
    private val batchUseCases: BatchUseCases
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
        viewModelScope.launch {
            batchUseCases.getAll().collect { list -> _state.update { it.copy(batches = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onTypeFilterChanged(t: String) = _state.update { it.copy(typeFilter = t) }
    fun toggleSort() = _state.update { it.copy(sortNewest = !it.sortNewest) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Expense) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        type: String,
        amount: Double,
        date: String,
        description: String?,
        purchaseId: String?,
        batchId: String?
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            if (existing == null) {
                useCases.create(
                    Expense(
                        id = UUID.randomUUID().toString(), lastModified = now,
                        type = type, amount = amount, date = date,
                        description = description, purchaseId = purchaseId, batchId = batchId
                    )
                )
            } else {
                useCases.update(
                    existing.copy(
                        type = type, amount = amount, date = date,
                        description = description, purchaseId = purchaseId,
                        batchId = batchId, lastModified = now
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
    private val purchaseUseCases: PurchaseUseCases,
    private val batchUseCases: BatchUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpensesViewModel(useCases, purchaseUseCases, batchUseCases) as T
    }
}
