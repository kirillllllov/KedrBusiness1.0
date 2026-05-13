package com.wholesale.manager.presentation.main.tabs.batches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField
import com.wholesale.manager.presentation.common.DatePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBatchDialog(
    editing: Batch?,
    nextBatchNumber: String,
    initialPurchasePrice: Double,
    initialExpensesTotal: Double,
    onDismiss: () -> Unit,
    onSave: (
        formationDate: String, rawQuantityKg: Double, outputPercent: Int,
        purchasePrice: Double, expensesTotal: Double, marketPricePerPercent: Double, status: String
    ) -> Unit
) {
    var formationDate by remember { mutableStateOf(editing?.formationDate ?: "") }
    var rawQuantityKg by remember { mutableStateOf(editing?.rawQuantityKg?.toString() ?: "") }
    var outputPercent by remember { mutableStateOf(editing?.outputPercent?.toString() ?: "") }
    var purchasePrice by remember { mutableStateOf(if (initialPurchasePrice > 0) String.format("%.2f", initialPurchasePrice) else "") }
    var expensesTotal by remember { mutableStateOf(if (initialExpensesTotal > 0) String.format("%.2f", initialExpensesTotal) else "") }
    var marketPricePerPercent by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(editing?.status ?: Batch.STATUS_ACTIVE) }

    val n = purchasePrice.toDoubleOrNull() ?: 0.0
    val r = expensesTotal.toDoubleOrNull() ?: 0.0
    val costPrice = n + r
    val v = outputPercent.toIntOrNull() ?: 0
    val h = marketPricePerPercent.toDoubleOrNull() ?: 0.0
    val optimalPrice = if (h > 0) costPrice + v * h else null

    var dateError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var outputError by remember { mutableStateOf(false) }
    var purchaseError by remember { mutableStateOf(false) }

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
                AppTextField(
                    value = rawQuantityKg,
                    onValueChange = { rawQuantityKg = it; quantityError = false },
                    label = "Сырьё (кг) *", isError = quantityError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                AppTextField(
                    value = outputPercent,
                    onValueChange = { outputPercent = it; outputError = false },
                    label = "Выход (%) *", isError = outputError, errorText = "Введите 1-100",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                HorizontalDivider()
                Text("Расчёт себестоимости", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                AppTextField(
                    value = purchasePrice,
                    onValueChange = { purchasePrice = it; purchaseError = false },
                    label = "n — закупочная стоимость (₽) *",
                    isError = purchaseError, errorText = "Введите число ≥ 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                AppTextField(
                    value = expensesTotal,
                    onValueChange = { expensesTotal = it },
                    label = "R — все расходы (обраб., хранение, транспорт) (₽)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("P = n + R", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text(
                            "${String.format("%.2f", costPrice)} ₽",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider()
                Text("Расчёт оптимальной цены", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                AppTextField(
                    value = marketPricePerPercent,
                    onValueChange = { marketPricePerPercent = it },
                    label = "h — рыночная цена за 1% выхода (₽)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Сезон:", style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically))
                    AssistChip(
                        onClick = { marketPricePerPercent = "10.0" },
                        label = { Text("Высокий (10 ₽)") }
                    )
                    AssistChip(
                        onClick = { marketPricePerPercent = "27.5" },
                        label = { Text("Низкий (27.5 ₽)") }
                    )
                }

                if (optimalPrice != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("So = P + v×h", style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium)
                            Text(
                                "${String.format("%.2f", optimalPrice)} ₽",
                                style = MaterialTheme.typography.bodyMedium,
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
                quantityError = rawQuantityKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                val pct = outputPercent.toIntOrNull()
                outputError = pct == null || pct !in 1..100
                purchaseError = purchasePrice.toDoubleOrNull()?.let { it < 0 } ?: true
                if (!dateError && !quantityError && !outputError && !purchaseError) {
                    onSave(
                        formationDate, rawQuantityKg.toDouble(), outputPercent.toInt(),
                        purchasePrice.toDoubleOrNull() ?: 0.0,
                        expensesTotal.toDoubleOrNull() ?: 0.0,
                        marketPricePerPercent.toDoubleOrNull() ?: 0.0,
                        status
                    )
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
