package com.wholesale.manager.presentation.main.tabs.batches

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

data class BatchesUiState(
    val items: List<Batch> = emptyList(),
    val allPurchases: List<PurchasedRaw> = emptyList(),
    val allExpenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: String = "",
    val showAddDialog: Boolean = false,
    val editingItem: Batch? = null
) {
    val filtered: List<Batch>
        get() = items.filter { b ->
            (searchQuery.isBlank() || b.number.contains(searchQuery, ignoreCase = true)) &&
                    (statusFilter.isBlank() || b.status == statusFilter)
        }

    val nextBatchNumber: String
        get() {
            val maxNum = items.maxOfOrNull { it.number.toIntOrNull() ?: 0 } ?: 0
            return (maxNum + 1).toString().padStart(3, '0')
        }

    fun linkedPurchasesSum(batchId: String?): Double =
        if (batchId == null) 0.0
        else allPurchases.filter { it.batchId == batchId }.sumOf { it.purchasePriceTotal }

    fun linkedExpensesSum(batchId: String?): Double =
        if (batchId == null) 0.0
        else allExpenses.filter { it.batchIds.contains(batchId) }.sumOf { it.amount }
}

class BatchesViewModel(
    private val useCases: BatchUseCases,
    private val purchaseUseCases: PurchaseUseCases,
    private val expenseUseCases: ExpenseUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(BatchesUiState())
    val state: StateFlow<BatchesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list -> _state.update { it.copy(items = list) } }
        }
        viewModelScope.launch {
            purchaseUseCases.getAll().collect { list -> _state.update { it.copy(allPurchases = list) } }
        }
        viewModelScope.launch {
            expenseUseCases.getAll().collect { list -> _state.update { it.copy(allExpenses = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onStatusFilterChanged(s: String) = _state.update { it.copy(statusFilter = s) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Batch) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        formationDate: String,
        rawQuantityKg: Double,
        outputPercent: Int,
        purchasePrice: Double,
        expensesTotal: Double,
        marketPricePerPercent: Double,
        status: String
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            val costPrice = purchasePrice + expensesTotal
            val optimalPricePerKg = if (marketPricePerPercent > 0)
                costPrice + outputPercent * marketPricePerPercent
            else null

            if (existing == null) {
                val number = _state.value.nextBatchNumber
                useCases.create(
                    Batch(
                        id = UUID.randomUUID().toString(),
                        number = number,
                        formationDate = formationDate,
                        rawQuantityKg = rawQuantityKg,
                        outputPercent = outputPercent,
                        costPrice = costPrice,
                        optimalPricePerKg = optimalPricePerKg,
                        status = status,
                        lastModified = now
                    )
                )
            } else {
                useCases.update(
                    existing.copy(
                        formationDate = formationDate,
                        rawQuantityKg = rawQuantityKg,
                        outputPercent = outputPercent,
                        costPrice = costPrice,
                        optimalPricePerKg = optimalPricePerKg,
                        status = status,
                        lastModified = now
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

class BatchesViewModelFactory(
    private val useCases: BatchUseCases,
    private val purchaseUseCases: PurchaseUseCases,
    private val expenseUseCases: ExpenseUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BatchesViewModel(useCases, purchaseUseCases, expenseUseCases) as T
    }
}
