package com.wholesale.manager.presentation.main.tabs.orders

import androidx.lifecycle.*
import com.wholesale.manager.di.BatchUseCases
import com.wholesale.manager.di.OrderUseCases
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.Order
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class OrdersUiState(
    val items: List<Order> = emptyList(),
    val batches: List<Batch> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: String = "",
    val showAddDialog: Boolean = false,
    val editingItem: Order? = null
) {
    val filtered: List<Order>
        get() = items.filter { o ->
            (searchQuery.isBlank() || o.customerName.contains(searchQuery, ignoreCase = true) ||
                    o.customerPhone.contains(searchQuery, ignoreCase = true)) &&
                    (statusFilter.isBlank() || o.status == statusFilter)
        }
}

class OrdersViewModel(
    private val useCases: OrderUseCases,
    private val batchUseCases: BatchUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list -> _state.update { it.copy(items = list) } }
        }
        viewModelScope.launch {
            batchUseCases.getAll().collect { list -> _state.update { it.copy(batches = list) } }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onStatusFilterChanged(s: String) = _state.update { it.copy(statusFilter = s) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Order) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        customerName: String, customerPhone: String, customerAddress: String?,
        batchId: String, quantityKg: Double, pricePerKg: Double,
        shipmentDate: String?, deliveryMethod: String, status: String
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            val total = quantityKg * pricePerKg
            if (existing == null) {
                useCases.create(Order(
                    id = UUID.randomUUID().toString(), lastModified = now,
                    customerName = customerName, customerPhone = customerPhone,
                    customerAddress = customerAddress, batchId = batchId,
                    quantityKg = quantityKg, pricePerKg = pricePerKg,
                    totalAmount = total, creationDate = now.substring(0, 10),
                    shipmentDate = shipmentDate, deliveryMethod = deliveryMethod, status = status
                ))
            } else {
                useCases.update(existing.copy(
                    customerName = customerName, customerPhone = customerPhone,
                    customerAddress = customerAddress, batchId = batchId,
                    quantityKg = quantityKg, pricePerKg = pricePerKg,
                    totalAmount = total, shipmentDate = shipmentDate,
                    deliveryMethod = deliveryMethod, status = status, lastModified = now
                ))
            }
            dismissDialog()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { useCases.delete(id) }
    }
}

class OrdersViewModelFactory(
    private val useCases: OrderUseCases,
    private val batchUseCases: BatchUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return OrdersViewModel(useCases, batchUseCases) as T
    }
}
