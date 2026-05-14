package com.wholesale.manager.presentation.main.tabs.purchases

import androidx.lifecycle.*
import com.wholesale.manager.di.BatchUseCases
import com.wholesale.manager.di.PurchaseUseCases
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.PurchasedRaw
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class PurchasesUiState(
    val items: List<PurchasedRaw> = emptyList(),
    val batches: List<Batch> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: String = "",
    val sortNewest: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingItem: PurchasedRaw? = null,
    val error: String? = null
) {
    val filtered: List<PurchasedRaw>
        get() {
            val base = items.filter { p ->
                (searchQuery.isBlank() || p.supplierName.contains(searchQuery, ignoreCase = true) ||
                        p.type.contains(searchQuery, ignoreCase = true)) &&
                        (statusFilter.isBlank() || p.status == statusFilter)
            }
            return if (sortNewest) base.sortedByDescending { it.purchaseDate }
            else base.sortedBy { it.purchaseDate }
        }

    val nextPurchaseNumber: Int
        get() = (items.maxOfOrNull { it.number } ?: 0) + 1
}

class PurchasesViewModel(
    private val useCases: PurchaseUseCases,
    private val batchUseCases: BatchUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(PurchasesUiState())
    val state: StateFlow<PurchasesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list -> _state.update { it.copy(items = list) } }
        }
        viewModelScope.launch {
            batchUseCases.getAll().collect { list -> _state.update { it.copy(batches = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onStatusFilterChanged(status: String) = _state.update { it.copy(statusFilter = status) }
    fun toggleSort() = _state.update { it.copy(sortNewest = !it.sortNewest) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: PurchasedRaw) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        type: String, quantityKg: Double, purchasePriceTotal: Double,
        supplierName: String, purchaseDate: String, status: String, batchId: String?
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            val pricePerKg = if (quantityKg > 0) purchasePriceTotal / quantityKg else 0.0
            if (existing == null) {
                useCases.create(
                    PurchasedRaw(
                        id = UUID.randomUUID().toString(),
                        number = _state.value.nextPurchaseNumber,
                        lastModified = now,
                        type = type, quantityKg = quantityKg, purchasePriceTotal = purchasePriceTotal,
                        pricePerKg = pricePerKg, supplierName = supplierName,
                        purchaseDate = purchaseDate, status = status, batchId = batchId
                    )
                )
            } else {
                useCases.update(
                    existing.copy(
                        type = type, quantityKg = quantityKg, purchasePriceTotal = purchasePriceTotal,
                        pricePerKg = pricePerKg, supplierName = supplierName,
                        purchaseDate = purchaseDate, status = status, batchId = batchId,
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

class PurchasesViewModelFactory(
    private val useCases: PurchaseUseCases,
    private val batchUseCases: BatchUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PurchasesViewModel(useCases, batchUseCases) as T
    }
}
