package com.wholesale.manager.presentation.main.tabs.batches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField
import com.wholesale.manager.presentation.common.DatePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBatchDialog(
    editing: Batch?,
    nextBatchNumber: String,
    availablePurchases: List<PurchasedRaw>,
    expensesForPurchase: (String?) -> Double,
    onDismiss: () -> Unit,
    onSave: (
        formationDate: String,
        purchaseId: String?,
        outputKg: Double,
        marketPricePerPercent: Double,
        status: String
    ) -> Unit
) {
    var formationDate by remember { mutableStateOf(editing?.formationDate ?: "") }
    var selectedPurchaseId by remember {
        mutableStateOf(editing?.purchaseId ?: availablePurchases.firstOrNull()?.id ?: "")
    }
    var purchaseDropdownExpanded by remember { mutableStateOf(false) }
    var outputKgText by remember {
        mutableStateOf(if (editing != null && editing.outputKg > 0) editing.outputKg.toString() else "")
    }
    var marketPricePerPercent by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(editing?.status ?: Batch.STATUS_ACTIVE) }

    var dateError by remember { mutableStateOf(false) }
    var outputError by remember { mutableStateOf(false) }

    val selectedPurchase = availablePurchases.find { it.id == selectedPurchaseId }
    val n = selectedPurchase?.purchasePriceTotal ?: 0.0
    val rawQty = selectedPurchase?.quantityKg ?: 0.0
    val r = expensesForPurchase(selectedPurchaseId.ifBlank { null })
    val costPrice = n + r
    val outputKg = outputKgText.toDoubleOrNull() ?: 0.0
    val outputPercent = if (rawQty > 0 && outputKg > 0) (outputKg / rawQty * 100.0) else 0.0
    val h = marketPricePerPercent.toDoubleOrNull() ?: 0.0
    val optimalPrice = if (h > 0 && outputPercent > 0) costPrice + outputPercent * h else null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editing == null) "Новая партия №$nextBatchNumber"
                else "Редактировать партию №${editing.number}"
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DatePickerField(
                    label = "Дата формирования *",
                    value = formationDate,
                    onValueChange = { formationDate = it; dateError = false },
                    isError = dateError,
                    errorText = "Обязательное поле"
                )

                ExposedDropdownMenuBox(
                    expanded = purchaseDropdownExpanded,
                    onExpandedChange = { purchaseDropdownExpanded = it }
                ) {
                    val purchaseDisplay = selectedPurchase?.let {
                        "Закупка №${it.number} · ${it.type} · ${String.format("%.0f", it.purchasePriceTotal)} ₽"
                    } ?: "Не выбрана"

                    OutlinedTextField(
                        value = purchaseDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Закупка *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = purchaseDropdownExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = purchaseDropdownExpanded,
                        onDismissRequest = { purchaseDropdownExpanded = false }
                    ) {
                        availablePurchases.forEach { p ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            "Закупка №${p.number} · ${p.type}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "${p.supplierName} · ${p.quantityKg} кг · ${String.format("%.0f", p.purchasePriceTotal)} ₽ · ${p.purchaseDate}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                },
                                onClick = {
                                    selectedPurchaseId = p.id
                                    purchaseDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedPurchase != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Исходное сырьё: ${selectedPurchase.quantityKg} кг (${selectedPurchase.type})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "n = стоимость закупки: ${String.format("%.0f", n)} ₽",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "R = расходы по закупке: ${String.format("%.0f", r)} ₽",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                HorizontalDivider()
                Text(
                    "Выход продукта",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                AppTextField(
                    value = outputKgText,
                    onValueChange = { outputKgText = it; outputError = false },
                    label = "Выход ореха 1 сорта (кг) *",
                    isError = outputError,
                    errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                if (outputPercent > 0) {
                    Text(
                        "Выход: ${String.format("%.1f", outputPercent)}% от исходной массы",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider()
                Text(
                    "Себестоимость партии",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "P = n + R",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "${String.format("%.0f", n)} + ${String.format("%.0f", r)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            "${String.format("%.2f", costPrice)} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider()
                Text(
                    "Оптимальная стоимость",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                AppTextField(
                    value = marketPricePerPercent,
                    onValueChange = { marketPricePerPercent = it },
                    label = "h — рыночная цена за 1% выхода (₽)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("10", "15", "20", "30").forEach { preset ->
                        AssistChip(
                            onClick = { marketPricePerPercent = preset },
                            label = { Text("$preset ₽") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (optimalPrice != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "So = P + v×h",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    "${String.format("%.2f", costPrice)} + ${String.format("%.1f", outputPercent)}×$h",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Text(
                                "${String.format("%.2f", optimalPrice)} ₽",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                HorizontalDivider()

                AppDropdown(
                    label = "Статус",
                    selected = status,
                    options = listOf(Batch.STATUS_ACTIVE to "Активна", Batch.STATUS_SOLD_OUT to "Продана"),
                    onSelected = { status = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                dateError = formationDate.isBlank()
                outputError = outputKgText.toDoubleOrNull()?.let { it <= 0 } ?: true
                if (!dateError && !outputError) {
                    onSave(
                        formationDate,
                        selectedPurchaseId.ifBlank { null },
                        outputKgText.toDouble(),
                        marketPricePerPercent.toDoubleOrNull() ?: 0.0,
                        status
                    )
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
