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
    val dateFrom: String = "",
    val dateTo: String = "",
    val sortNewest: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingItem: Order? = null
) {
    val filtered: List<Order>
        get() {
            val base = items.filter { o ->
                (searchQuery.isBlank() || o.customerName.contains(searchQuery, ignoreCase = true) ||
                        o.customerPhone.contains(searchQuery, ignoreCase = true)) &&
                        (statusFilter.isBlank() || o.status == statusFilter) &&
                        (dateFrom.isBlank() || o.creationDate >= dateFrom) &&
                        (dateTo.isBlank() || o.creationDate <= dateTo)
            }
            return if (sortNewest) base.sortedByDescending { it.creationDate }
            else base.sortedBy { it.creationDate }
        }

    val totalAmount: Double get() = filtered.sumOf { it.totalAmount }
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
    fun onDateFromChanged(d: String) = _state.update { it.copy(dateFrom = d) }
    fun onDateToChanged(d: String) = _state.update { it.copy(dateTo = d) }
    fun clearDateRange() = _state.update { it.copy(dateFrom = "", dateTo = "") }
    fun toggleSort() = _state.update { it.copy(sortNewest = !it.sortNewest) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Order) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun updateStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            val order = _state.value.items.find { it.id == orderId } ?: return@launch
            useCases.update(order.copy(status = newStatus, lastModified = Instant.now().toString()))
        }
    }

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
                useCases.create(
                    Order(
                        id = UUID.randomUUID().toString(), lastModified = now,
                        customerName = customerName, customerPhone = customerPhone,
                        customerAddress = customerAddress, batchId = batchId,
                        quantityKg = quantityKg, pricePerKg = pricePerKg,
                        totalAmount = total, creationDate = now.substring(0, 10),
                        shipmentDate = shipmentDate, deliveryMethod = deliveryMethod, status = status
                    )
                )
            } else {
                useCases.update(
                    existing.copy(
                        customerName = customerName, customerPhone = customerPhone,
                        customerAddress = customerAddress, batchId = batchId,
                        quantityKg = quantityKg, pricePerKg = pricePerKg,
                        totalAmount = total, shipmentDate = shipmentDate,
                        deliveryMethod = deliveryMethod, status = status, lastModified = now
                    )
                )
            }
            checkAndUpdateBatchSoldOut(batchId, quantityKg, existing?.id, now)
            dismissDialog()
        }
    }

    private suspend fun checkAndUpdateBatchSoldOut(
        batchId: String, newQuantityKg: Double, existingOrderId: String?, now: String
    ) {
        val batch = _state.value.batches.find { it.id == batchId } ?: return
        val availableKg = batch.outputKg
        val totalOrdered = _state.value.items
            .filter { it.batchId == batchId && it.status != Order.STATUS_CANCELLED }
            .filter { it.id != existingOrderId }
            .sumOf { it.quantityKg } + newQuantityKg
        if (totalOrdered >= availableKg && availableKg > 0 && batch.status != Batch.STATUS_SOLD_OUT) {
            batchUseCases.update(batch.copy(status = Batch.STATUS_SOLD_OUT, lastModified = now))
        } else if (totalOrdered < availableKg && batch.status == Batch.STATUS_SOLD_OUT) {
            batchUseCases.update(batch.copy(status = Batch.STATUS_ACTIVE, lastModified = now))
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
