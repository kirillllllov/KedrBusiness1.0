package com.wholesale.manager.presentation.main.tabs.orders

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
import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.model.User
import com.wholesale.manager.presentation.auth.Permissions
import com.wholesale.manager.presentation.common.ConfirmDeleteDialog
import com.wholesale.manager.presentation.common.FilterChipRow
import com.wholesale.manager.presentation.common.SearchBar
import com.wholesale.manager.presentation.common.SwipeToDeleteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersTab(viewModel: OrdersViewModel, userRole: String) {
    val state by viewModel.state.collectAsState()
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    if (pendingDeleteId != null) {
        ConfirmDeleteDialog(
            message = "Заказ будет удалён. Это действие нельзя отменить.",
            onConfirm = { viewModel.delete(pendingDeleteId!!); pendingDeleteId = null },
            onDismiss = { pendingDeleteId = null }
        )
    }

    if (state.showAddDialog) {
        AddEditOrderDialog(
            editing = state.editingItem,
            availableBatches = state.batches,
            onDismiss = { viewModel.dismissDialog() },
            onSave = { name, phone, addr, batchId, qty, price, shipDate, delivery, status ->
                viewModel.save(name, phone, addr, batchId, qty, price, shipDate, delivery, status)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заказы", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (Permissions.canCreateOrder(userRole)) {
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
                placeholder = "Поиск по клиенту или телефону...")
            FilterChipRow(
                options = listOf(
                    "" to "Все",
                    Order.STATUS_NEW to "Новый",
                    Order.STATUS_CONFIRMED to "Подтверждён",
                    Order.STATUS_SHIPPED to "Отправлен",
                    Order.STATUS_DELIVERED to "Доставлен",
                    Order.STATUS_CANCELLED to "Отменён"
                ),
                selected = state.statusFilter,
                onSelected = viewModel::onStatusFilterChanged
            )
            if (state.filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Receipt, null, modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Нет заказов", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered, key = { it.id }) { item ->
                        if (userRole == User.ROLE_EXECUTOR) {
                            ExecutorOrderCard(
                                item = item,
                                onStatusUpdate = { newStatus -> viewModel.updateStatus(item.id, newStatus) }
                            )
                        } else {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    when (value) {
                                        EndToStart -> { pendingDeleteId = item.id; false }
                                        StartToEnd -> { viewModel.showEditDialog(item); false }
                                        else -> false
                                    }
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = { SwipeToDeleteBackground(dismissState) },
                                content = {
                                    OrderCard(item, onClick = { viewModel.showEditDialog(item) })
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutorOrderCard(item: Order, onStatusUpdate: (String) -> Unit) {
    var statusMenuExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf(
        Order.STATUS_NEW to "Новый",
        Order.STATUS_CONFIRMED to "Подтверждён",
        Order.STATUS_SHIPPED to "Отправлен",
        Order.STATUS_DELIVERED to "Доставлен"
    )

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.customerName, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)
                    Text(item.customerPhone, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline)
                }
                OrderStatusChip(item.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                OrderInfoItem("Кол-во", "${item.quantityKg} кг")
                OrderInfoItem("Итого", "${String.format("%.0f", item.totalAmount)} ₽")
                item.shipmentDate?.let { OrderInfoItem("Отгрузка", it) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = statusMenuExpanded,
                onExpandedChange = { statusMenuExpanded = it }
            ) {
                OutlinedButton(
                    onClick = { statusMenuExpanded = true },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                ) {
                    Text("Изменить статус")
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Filled.ArrowDropDown, null)
                }
                ExposedDropdownMenu(
                    expanded = statusMenuExpanded,
                    onDismissRequest = { statusMenuExpanded = false }
                ) {
                    statusOptions.forEach { (status, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onStatusUpdate(status)
                                statusMenuExpanded = false
                            },
                            leadingIcon = if (item.status == status) {
                                { Icon(Icons.Filled.Check, null, tint = MaterialTheme.colorScheme.primary) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(item: Order, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.customerName, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)
                    Text(item.customerPhone, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline)
                }
                OrderStatusChip(item.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                OrderInfoItem("Кол-во", "${item.quantityKg} кг")
                OrderInfoItem("Цена/кг", "${String.format("%.2f", item.pricePerKg)} ₽")
                OrderInfoItem("Итого", "${String.format("%.0f", item.totalAmount)} ₽")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(orderDeliveryLabel(item.deliveryMethod), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline)
                item.shipmentDate?.let {
                    Text("Отгрузка: $it", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun OrderInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun OrderStatusChip(status: String) {
    val (label, color) = when (status) {
        Order.STATUS_NEW -> "Новый" to Color(0xFF1565C0)
        Order.STATUS_CONFIRMED -> "Подтверждён" to Color(0xFF6A1B9A)
        Order.STATUS_SHIPPED -> "Отправлен" to Color(0xFFF57C00)
        Order.STATUS_DELIVERED -> "Доставлен" to Color(0xFF2E7D32)
        Order.STATUS_CANCELLED -> "Отменён" to Color(0xFFBA1A1A)
        else -> status to Color.Gray
    }
    Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
        Text(label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
    }
}

fun orderDeliveryLabel(method: String) = when (method) {
    Order.DELIVERY_PICKUP -> "Самовывоз"
    Order.DELIVERY_COURIER -> "Курьер"
    Order.DELIVERY_TRANSPORT -> "Транспортная"
    else -> method
}
