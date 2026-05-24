package com.wholesale.manager.presentation.main.tabs.expenses

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
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.presentation.auth.Permissions
import com.wholesale.manager.presentation.common.ConfirmDeleteDialog
import com.wholesale.manager.presentation.common.FilterChipRow
import com.wholesale.manager.presentation.common.SearchBar
import com.wholesale.manager.presentation.common.SwipeToDeleteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesTab(viewModel: ExpensesViewModel, userRole: String) {
    val state by viewModel.state.collectAsState()
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    if (pendingDeleteId != null) {
        ConfirmDeleteDialog(
            message = "Расход будет удалён. Это действие нельзя отменить.",
            onConfirm = { viewModel.delete(pendingDeleteId!!); pendingDeleteId = null },
            onDismiss = { pendingDeleteId = null }
        )
    }

    if (state.showAddDialog) {
        AddEditExpenseDialog(
            editing = state.editingItem,
            availablePurchases = state.purchases,
            availableBatches = state.batches,
            onDismiss = { viewModel.dismissDialog() },
            onSave = { type, amount, date, description, purchaseId, batchId ->
                viewModel.save(type, amount, date, description, purchaseId, batchId)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расходы", fontWeight = FontWeight.Bold) },
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
            if (Permissions.canAddExpense(userRole)) {
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
            if (state.filtered.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Итого:", style = MaterialTheme.typography.titleSmall)
                        Text("${String.format("%.0f", state.totalAmount)} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            SearchBar(query = state.searchQuery, onQueryChanged = viewModel::onSearchChanged,
                placeholder = "Поиск по типу или описанию...")
            FilterChipRow(
                options = listOf(
                    "" to "Все",
                    Expense.TYPE_TRANSPORT to "Транспорт",
                    Expense.TYPE_PROCESSING to "Переработка",
                    Expense.TYPE_STORAGE to "Хранение",
                    Expense.TYPE_SALARY to "Зарплата",
                    Expense.TYPE_UTILITY to "Коммунальные",
                    Expense.TYPE_EQUIPMENT to "Оборудование",
                    Expense.TYPE_OTHER to "Прочее"
                ),
                selected = state.typeFilter,
                onSelected = viewModel::onTypeFilterChanged
            )
            if (state.filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AttachMoney, null,
                            modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Нет расходов", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered, key = { it.id }) { item ->
                        val linkedPurchase = state.purchases.find { it.id == item.purchaseId }
                        val linkedBatch = state.batches.find { it.id == item.batchId }
                        if (Permissions.canDeleteExpense(userRole) || Permissions.canEditExpense(userRole)) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    when (value) {
                                        EndToStart -> {
                                            if (Permissions.canDeleteExpense(userRole)) pendingDeleteId = item.id
                                            false
                                        }
                                        StartToEnd -> {
                                            if (Permissions.canEditExpense(userRole)) viewModel.showEditDialog(item)
                                            false
                                        }
                                        else -> false
                                    }
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = { SwipeToDeleteBackground(dismissState) },
                                content = {
                                    ExpenseCard(
                                        item = item,
                                        purchaseLabel = linkedPurchase?.let { "Закупка №${it.number} · ${it.type}" },
                                        batchLabel = linkedBatch?.let { "Партия №${it.number}" },
                                        onClick = { if (Permissions.canEditExpense(userRole)) viewModel.showEditDialog(item) }
                                    )
                                }
                            )
                        } else {
                            ExpenseCard(
                                item = item,
                                purchaseLabel = linkedPurchase?.let { "Закупка №${it.number} · ${it.type}" },
                                batchLabel = linkedBatch?.let { "Партия №${it.number}" },
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(
    item: Expense,
    purchaseLabel: String?,
    batchLabel: String?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expenseTypeLabel(item.type), fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall)
                item.description?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                }
                Text(item.date, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline)
                if (purchaseLabel != null) {
                    Text(purchaseLabel, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary)
                }
                if (batchLabel != null) {
                    Text(batchLabel, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("${String.format("%.0f", item.amount)} ₽",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error)
        }
    }
}

fun expenseTypeLabel(type: String) = when (type) {
    Expense.TYPE_TRANSPORT -> "Транспорт"
    Expense.TYPE_PROCESSING -> "Переработка"
    Expense.TYPE_STORAGE -> "Хранение"
    Expense.TYPE_SALARY -> "Зарплата"
    Expense.TYPE_UTILITY -> "Коммунальные"
    Expense.TYPE_EQUIPMENT -> "Оборудование"
    Expense.TYPE_OTHER -> "Прочее"
    else -> type
}
