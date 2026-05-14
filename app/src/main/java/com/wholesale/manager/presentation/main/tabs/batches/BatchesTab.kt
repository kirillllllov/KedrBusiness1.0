package com.wholesale.manager.presentation.main.tabs.batches

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.presentation.common.FilterChipRow
import com.wholesale.manager.presentation.common.SearchBar
import com.wholesale.manager.presentation.common.SwipeToDeleteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchesTab(viewModel: BatchesViewModel) {
    val state by viewModel.state.collectAsState()

    if (state.showAddDialog) {
        val editing = state.editingItem
        AddEditBatchDialog(
            editing = editing,
            nextBatchNumber = state.nextBatchNumber,
            availablePurchases = state.allPurchases,
            expensesForPurchase = { purchaseId -> state.expensesTotalForPurchase(purchaseId) },
            onDismiss = { viewModel.dismissDialog() },
            onSave = { formationDate, purchaseId, outputKg, marketPrice, status ->
                viewModel.save(formationDate, purchaseId, outputKg, marketPrice, status)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Партии", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SearchBar(
                query = state.searchQuery,
                onQueryChanged = viewModel::onSearchChanged,
                placeholder = "Поиск по номеру партии..."
            )
            FilterChipRow(
                options = listOf(
                    "" to "Все",
                    Batch.STATUS_ACTIVE to "Активна",
                    Batch.STATUS_SOLD_OUT to "Продана"
                ),
                selected = state.statusFilter,
                onSelected = viewModel::onStatusFilterChanged
            )
            if (state.filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Inventory, null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Нет партий", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered, key = { it.id }) { item ->
                        val linkedPurchase = state.allPurchases.find { it.id == item.purchaseId }
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                when (value) {
                                    EndToStart -> { viewModel.delete(item.id); true }
                                    StartToEnd -> { viewModel.showEditDialog(item); false }
                                    else -> false
                                }
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = { SwipeToDeleteBackground(dismissState) },
                            content = {
                                BatchCard(
                                    item = item,
                                    purchaseInfo = linkedPurchase?.let { "Закупка №${it.number} · ${it.type}" },
                                    expensesTotal = state.expensesTotalForPurchase(item.purchaseId),
                                    onClick = { viewModel.showEditDialog(item) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BatchCard(
    item: Batch,
    purchaseInfo: String?,
    expensesTotal: Double,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Партия №${item.number}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        item.formationDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    if (purchaseInfo != null) {
                        Text(
                            purchaseInfo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                BatchStatusChip(item.status)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (item.rawQuantityKg > 0) {
                    BatchInfoItem("Сырьё", "${item.rawQuantityKg} кг")
                }
                if (item.outputKg > 0) {
                    BatchInfoItem("Выход", "${item.outputKg} кг")
                }
                if (item.outputPercent > 0) {
                    BatchInfoItem("%", "${item.outputPercent}%")
                }
                BatchInfoItem("Себест.", "${String.format("%.0f", item.costPrice)} ₽")
                item.optimalPricePerKg?.let {
                    BatchInfoItem("So", "${String.format("%.0f", it)} ₽")
                }
            }
            if (expensesTotal > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Расходы по закупке:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        "${String.format("%.0f", expensesTotal)} ₽",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun BatchInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BatchStatusChip(status: String) {
    val (label, color) = when (status) {
        Batch.STATUS_ACTIVE -> "Активна" to Color(0xFF2E7D32)
        Batch.STATUS_SOLD_OUT -> "Продана" to Color(0xFF616161)
        else -> status to Color.Gray
    }
    Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}
