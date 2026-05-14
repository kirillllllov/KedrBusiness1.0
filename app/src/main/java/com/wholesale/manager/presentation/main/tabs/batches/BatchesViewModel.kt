package com.wholesale.manager.presentation.main.tabs.batches

import androidx.lifecycle.*
import com.wholesale.manager.di.BatchUseCases
import com.wholesale.manager.di.ExpenseUseCases
import com.wholesale.manager.di.OrderUseCases
import com.wholesale.manager.di.PurchaseUseCases
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.model.PurchasedRaw
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import kotlin.math.roundToInt

data class BatchesUiState(
    val items: List<Batch> = emptyList(),
    val allPurchases: List<PurchasedRaw> = emptyList(),
    val allExpenses: List<Expense> = emptyList(),
    val allOrders: List<Order> = emptyList(),
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

    fun expensesTotalForPurchase(purchaseId: String?): Double =
        if (purchaseId == null) 0.0
        else allExpenses.filter { it.purchaseId == purchaseId }.sumOf { it.amount }

    fun purchaseById(purchaseId: String?): PurchasedRaw? =
        allPurchases.find { it.id == purchaseId }

    fun remainingKg(batchId: String, outputKg: Double): Double {
        val ordered = allOrders
            .filter { it.batchId == batchId && it.status != Order.STATUS_CANCELLED }
            .sumOf { it.quantityKg }
        return (outputKg - ordered).coerceAtLeast(0.0)
    }
}

class BatchesViewModel(
    private val useCases: BatchUseCases,
    private val purchaseUseCases: PurchaseUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val orderUseCases: OrderUseCases
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
        viewModelScope.launch {
            orderUseCases.getAll().collect { list -> _state.update { it.copy(allOrders = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onStatusFilterChanged(s: String) = _state.update { it.copy(statusFilter = s) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Batch) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        formationDate: String,
        purchaseId: String?,
        outputKg: Double,
        marketPricePerPercent: Double,
        status: String
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            val st = _state.value

            val purchase = st.purchaseById(purchaseId)
            val n = purchase?.purchasePriceTotal ?: 0.0
            val rawQty = purchase?.quantityKg ?: 0.0
            val r = st.expensesTotalForPurchase(purchaseId)
            val costPrice = n + r
            val outputPercent = if (rawQty > 0) (outputKg / rawQty * 100).roundToInt() else 0
            val pPerKg = if (outputKg > 0) costPrice / outputKg else 0.0
            val optimalPricePerKg = if (marketPricePerPercent > 0 && outputPercent > 0)
                pPerKg + outputPercent * marketPricePerPercent
            else null

            if (existing == null) {
                useCases.create(
                    Batch(
                        id = UUID.randomUUID().toString(),
                        number = st.nextBatchNumber,
                        formationDate = formationDate,
                        purchaseId = purchaseId,
                        rawQuantityKg = rawQty,
                        outputKg = outputKg,
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
                        purchaseId = purchaseId,
                        rawQuantityKg = rawQty,
                        outputKg = outputKg,
                        outputPercent = outputPercent,
                        costPrice = costPrice,
                        optimalPricePerKg = optimalPricePerKg,
                        status = status,
                        lastModified = now
                    )
                )
            }

            if (purchase != null && purchase.status != PurchasedRaw.STATUS_IN_BATCH) {
                purchaseUseCases.update(
                    purchase.copy(
                        status = PurchasedRaw.STATUS_IN_BATCH,
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
    private val expenseUseCases: ExpenseUseCases,
    private val orderUseCases: OrderUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BatchesViewModel(useCases, purchaseUseCases, expenseUseCases, orderUseCases) as T
    }
}
