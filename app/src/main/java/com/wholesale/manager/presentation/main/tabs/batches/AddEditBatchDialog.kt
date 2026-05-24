package com.wholesale.manager.presentation.main.tabs.batches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import java.time.LocalDate

private val EXPENSE_TYPES = listOf(
    Expense.TYPE_PROCESSING to "Переработка",
    Expense.TYPE_STORAGE to "Хранение",
    Expense.TYPE_TRANSPORT to "Транспорт",
    Expense.TYPE_SALARY to "Зарплата / Грузчики",
    Expense.TYPE_UTILITY to "Электроэнергия",
    Expense.TYPE_EQUIPMENT to "Тех. обслуживание",
    Expense.TYPE_OTHER to "Прочее"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBatchDialog(
    editing: Batch?,
    nextBatchNumber: String,
    availablePurchases: List<PurchasedRaw>,
    expensesForPurchases: (List<String>) -> Double,
    onDismiss: () -> Unit,
    onSave: (
        formationDate: String,
        purchaseIds: List<String>,
        outputKg: Double,
        gradeOnePercent: Int,
        marketPricePerPercent: Double,
        status: String,
        batchExpenses: List<BatchExpenseEntry>
    ) -> Unit
) {
    val today = LocalDate.now().toString()
    var formationDate by remember { mutableStateOf(editing?.formationDate ?: today) }

    var selectedPurchaseIds by remember {
        mutableStateOf(editing?.purchaseIds?.toSet() ?: emptySet())
    }

    var outputKgText by remember {
        mutableStateOf(if (editing != null && editing.outputKg > 0) editing.outputKg.toString() else "")
    }
    var gradeOnePercentText by remember {
        mutableStateOf(if (editing != null && editing.gradeOnePercent > 0) editing.gradeOnePercent.toString() else "")
    }
    var marketPricePerPercent by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(editing?.status ?: Batch.STATUS_ACTIVE) }

    var batchExpenses by remember { mutableStateOf<List<BatchExpenseEntry>>(emptyList()) }
    var newExpenseType by remember { mutableStateOf(Expense.TYPE_PROCESSING) }
    var newExpenseAmount by remember { mutableStateOf("") }
    var newExpenseDescription by remember { mutableStateOf("") }
    var showAddExpenseRow by remember { mutableStateOf(false) }
    var newExpenseTypeExpanded by remember { mutableStateOf(false) }
    var newExpenseAmountError by remember { mutableStateOf(false) }

    var dateError by remember { mutableStateOf(false) }
    var outputError by remember { mutableStateOf(false) }

    val selectedPurchases = availablePurchases.filter { it.id in selectedPurchaseIds }
    val n = selectedPurchases.sumOf { it.purchasePriceTotal }
    val rawQty = selectedPurchases.sumOf { it.quantityKg }
    val rPurchases = expensesForPurchases(selectedPurchaseIds.toList())
    val rBatch = batchExpenses.sumOf { it.amount }
    val costPrice = n + rPurchases + rBatch
    val outputKg = outputKgText.toDoubleOrNull() ?: 0.0
    val outputPercent = if (rawQty > 0 && outputKg > 0) (outputKg / rawQty * 100.0) else 0.0
    val gradeOnePercent = gradeOnePercentText.toIntOrNull() ?: 0
    val pPerKg = if (outputKg > 0) costPrice / outputKg else 0.0
    val h = marketPricePerPercent.toDoubleOrNull() ?: 0.0
    val optimalPricePerKg = if (h > 0 && gradeOnePercent > 0) pPerKg + gradeOnePercent * h else null

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
                    label = "Дата формирования",
                    value = formationDate,
                    onValueChange = { formationDate = it; dateError = false },
                    isError = dateError,
                    errorText = "Обязательное поле"
                )

                HorizontalDivider()
                Text(
                    "Закупки сырья",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Выберите одну или несколько закупок:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                if (availablePurchases.isEmpty()) {
                    Text(
                        "Нет доступных закупок",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    availablePurchases.forEach { p ->
                        val isChecked = p.id in selectedPurchaseIds
                        Surface(
                            color = if (isChecked) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.small,
                            tonalElevation = if (isChecked) 0.dp else 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        selectedPurchaseIds = if (checked)
                                            selectedPurchaseIds + p.id
                                        else
                                            selectedPurchaseIds - p.id
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(modifier = Modifier.weight(1f)) {
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
                            }
                        }
                    }
                }

                if (selectedPurchases.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Итого сырьё: ${String.format("%.1f", rawQty)} кг",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "n = стоимость закупок: ${String.format("%.0f", n)} ₽",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "R = расходы по закупкам: ${String.format("%.0f", rPurchases)} ₽",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                HorizontalDivider()
                Text(
                    "Расходы на партию",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Переработка, хранение и другие расходы, связанные с партией:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                batchExpenses.forEachIndexed { index, entry ->
                    val typeName = EXPENSE_TYPES.firstOrNull { it.first == entry.type }?.second ?: entry.type
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    typeName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                if (!entry.description.isNullOrBlank()) {
                                    Text(
                                        entry.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                            Text(
                                "${String.format("%.0f", entry.amount)} ₽",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            IconButton(
                                onClick = { batchExpenses = batchExpenses.toMutableList().also { it.removeAt(index) } },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Удалить",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                if (showAddExpenseRow) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Новый расход на партию",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                            ExposedDropdownMenuBox(
                                expanded = newExpenseTypeExpanded,
                                onExpandedChange = { newExpenseTypeExpanded = it }
                            ) {
                                val typeName = EXPENSE_TYPES.firstOrNull { it.first == newExpenseType }?.second ?: newExpenseType
                                OutlinedTextField(
                                    value = typeName,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Тип расхода") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = newExpenseTypeExpanded) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = newExpenseTypeExpanded,
                                    onDismissRequest = { newExpenseTypeExpanded = false }
                                ) {
                                    EXPENSE_TYPES.forEach { (key, label) ->
                                        DropdownMenuItem(
                                            text = { Text(label) },
                                            onClick = { newExpenseType = key; newExpenseTypeExpanded = false }
                                        )
                                    }
                                }
                            }
                            AppTextField(
                                value = newExpenseAmount,
                                onValueChange = { newExpenseAmount = it; newExpenseAmountError = false },
                                label = "Сумма (₽) *",
                                isError = newExpenseAmountError,
                                errorText = "Введите число > 0",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            AppTextField(
                                value = newExpenseDescription,
                                onValueChange = { newExpenseDescription = it },
                                label = "Описание (опц.)",
                                maxLines = 2
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        showAddExpenseRow = false
                                        newExpenseAmount = ""
                                        newExpenseDescription = ""
                                        newExpenseAmountError = false
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Отмена") }
                                Button(
                                    onClick = {
                                        newExpenseAmountError = newExpenseAmount.toDoubleOrNull()?.let { it <= 0 } ?: true
                                        if (!newExpenseAmountError) {
                                            batchExpenses = batchExpenses + BatchExpenseEntry(
                                                type = newExpenseType,
                                                amount = newExpenseAmount.toDouble(),
                                                description = newExpenseDescription.ifBlank { null }
                                            )
                                            showAddExpenseRow = false
                                            newExpenseAmount = ""
                                            newExpenseDescription = ""
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Добавить") }
                            }
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { showAddExpenseRow = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Добавить расход на партию")
                    }
                }

                if (rBatch > 0) {
                    Text(
                        "R_партия = ${String.format("%.0f", rBatch)} ₽",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
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
                    label = "Выход ореха (кг, всего) *",
                    isError = outputError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                if (outputPercent > 0) {
                    Text(
                        "Выход: ${String.format("%.1f", outputPercent)}% от исходной массы",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                AppTextField(
                    value = gradeOnePercentText,
                    onValueChange = { gradeOnePercentText = it.filter { c -> c.isDigit() } },
                    label = "Процент 1-го сорта (%) *",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (gradeOnePercent > 0 && outputKg > 0) {
                    val gradeOneKg = outputKg * gradeOnePercent / 100.0
                    Text(
                        "1-й сорт: ${String.format("%.1f", gradeOneKg)} кг (${gradeOnePercent}% от ${String.format("%.1f", outputKg)} кг)",
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "P = n + R_закупки + R_партия",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "= ${String.format("%.0f", n)} + ${String.format("%.0f", rPurchases)} + ${String.format("%.0f", rBatch)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "P (всего): ${String.format("%.2f", costPrice)} ₽",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            if (outputKg > 0) {
                                Text(
                                    "P за кг: ${String.format("%.2f", pPerKg)} ₽/кг",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
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
                    "Оптимальная стоимость (за кг)",
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

                if (optimalPricePerKg != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "So(за кг) = P(за кг) + v₁×h",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    "v₁ = ${gradeOnePercent}% (1-й сорт)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    "${String.format("%.2f", pPerKg)} + ${gradeOnePercent}×${String.format("%.0f", h)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Text(
                                "${String.format("%.2f", optimalPricePerKg)} ₽/кг",
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
                        selectedPurchaseIds.toList(),
                        outputKgText.toDouble(),
                        gradeOnePercent,
                        marketPricePerPercent.toDoubleOrNull() ?: 0.0,
                        status,
                        batchExpenses
                    )
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
