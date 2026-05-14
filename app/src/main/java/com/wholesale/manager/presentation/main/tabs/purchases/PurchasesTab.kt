package com.wholesale.manager.presentation.main.tabs.purchases

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
import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.presentation.auth.Permissions
import com.wholesale.manager.presentation.common.ConfirmDeleteDialog
import com.wholesale.manager.presentation.common.FilterChipRow
import com.wholesale.manager.presentation.common.SearchBar
import com.wholesale.manager.presentation.common.SwipeToDeleteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesTab(viewModel: PurchasesViewModel, userRole: String) {
    val state by viewModel.state.collectAsState()
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    if (pendingDeleteId != null) {
        ConfirmDeleteDialog(
            message = "Закупка будет удалена. Это действие нельзя отменить.",
            onConfirm = { viewModel.delete(pendingDeleteId!!); pendingDeleteId = null },
            onDismiss = { pendingDeleteId = null }
        )
    }

    if (state.showAddDialog) {
        AddEditPurchaseDialog(
            editing = state.editingItem,
            nextPurchaseNumber = state.nextPurchaseNumber,
            availableBatches = state.batches,
            onDismiss = { viewModel.dismissDialog() },
            onSave = { type, qty, total, supplier, date, status, batchId ->
                viewModel.save(type, qty, total, supplier, date, status, batchId)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Закупки", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { viewModel.toggleSort() }) {
                        Icon(
                            if (state.sortNewest) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                            contentDescription = if (state.sortNewest) "Сначала новые" else "Сначала старые"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (Permissions.canAddPurchase(userRole)) {
                FloatingActionButton(
                    onClick = { viewModel.showAddDialog() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Добавить", tint = Color.White)
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SearchBar(query = state.searchQuery, onQueryChanged = viewModel::onSearchChanged,
                placeholder = "Поиск по поставщику или типу...")
            FilterChipRow(
                options = listOf(
                    "" to "Все",
                    PurchasedRaw.STATUS_PENDING to "Ожидает",
                    PurchasedRaw.STATUS_IN_BATCH to "В партии",
                    PurchasedRaw.STATUS_PROCESSED to "Обработано"
                ),
                selected = state.statusFilter,
                onSelected = viewModel::onStatusFilterChanged
            )
            if (state.filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.ShoppingCart, null, modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Нет закупок", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered, key = { it.id }) { item ->
                        if (Permissions.canDeletePurchase(userRole) || Permissions.canEditPurchase(userRole)) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    when (value) {
                                        EndToStart -> { if (Permissions.canDeletePurchase(userRole)) pendingDeleteId = item.id; false }
                                        StartToEnd -> { if (Permissions.canEditPurchase(userRole)) viewModel.showEditDialog(item); false }
                                        else -> false
                                    }
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = { SwipeToDeleteBackground(dismissState) },
                                content = {
                                    PurchaseCard(item, onClick = {
                                        if (Permissions.canEditPurchase(userRole)) viewModel.showEditDialog(item)
                                    })
                                }
                            )
                        } else {
                            PurchaseCard(item, onClick = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseCard(item: PurchasedRaw, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Закупка №${item.number} · ${item.type}",
                        fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                    Text(item.supplierName, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusChip(item.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                InfoItem("Кол-во", "${item.quantityKg} кг")
                InfoItem("Цена/кг", "${String.format("%.2f", item.pricePerKg)} ₽")
                InfoItem("Сумма", "${String.format("%.0f", item.purchasePriceTotal)} ₽")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.purchaseDate, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun StatusChip(status: String) {
    val (label, color) = when (status) {
        PurchasedRaw.STATUS_PENDING -> "Ожидает" to Color(0xFFF57C00)
        PurchasedRaw.STATUS_IN_BATCH -> "В партии" to Color(0xFF1565C0)
        PurchasedRaw.STATUS_PROCESSED -> "Обработано" to Color(0xFF2E7D32)
        else -> status to Color.Gray
    }
    Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
        Text(label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
    }
}
