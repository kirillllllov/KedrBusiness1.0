package com.wholesale.manager.presentation.main.tabs.batches

import androidx.lifecycle.*
import com.wholesale.manager.di.BatchUseCases
import com.wholesale.manager.domain.model.Batch
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

data class BatchesUiState(
    val items: List<Batch> = emptyList(),
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
}

class BatchesViewModel(private val useCases: BatchUseCases) : ViewModel() {
    private val _state = MutableStateFlow(BatchesUiState())
    val state: StateFlow<BatchesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAll().collect { list ->
                _state.update { it.copy(items = list) }
            }
        }
    }

    fun onSearchChanged(query: String) = _state.update { it.copy(searchQuery = query) }
    fun onStatusFilterChanged(s: String) = _state.update { it.copy(statusFilter = s) }
    fun showAddDialog() = _state.update { it.copy(showAddDialog = true, editingItem = null) }
    fun showEditDialog(item: Batch) = _state.update { it.copy(showAddDialog = true, editingItem = item) }
    fun dismissDialog() = _state.update { it.copy(showAddDialog = false, editingItem = null) }

    fun save(
        number: String, formationDate: String, rawQuantityKg: Double,
        outputPercent: Int, costPrice: Double, optimalPricePerKg: Double?, status: String
    ) {
        viewModelScope.launch {
            val existing = _state.value.editingItem
            val now = Instant.now().toString()
            if (existing == null) {
                useCases.create(Batch(
                    id = UUID.randomUUID().toString(), number = number,
                    formationDate = formationDate, rawQuantityKg = rawQuantityKg,
                    outputPercent = outputPercent, costPrice = costPrice,
                    optimalPricePerKg = optimalPricePerKg, status = status, lastModified = now
                ))
            } else {
                useCases.update(existing.copy(
                    number = number, formationDate = formationDate,
                    rawQuantityKg = rawQuantityKg, outputPercent = outputPercent,
                    costPrice = costPrice, optimalPricePerKg = optimalPricePerKg,
                    status = status, lastModified = now
                ))
            }
            dismissDialog()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { useCases.delete(id) }
    }
}

class BatchesViewModelFactory(private val useCases: BatchUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BatchesViewModel(useCases) as T
    }
}
