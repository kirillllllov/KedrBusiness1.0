package com.wholesale.manager.presentation.main.tabs.batches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField

@Composable
fun AddEditBatchDialog(
    editing: Batch?,
    onDismiss: () -> Unit,
    onSave: (number: String, formationDate: String, rawQuantityKg: Double,
             outputPercent: Int, costPrice: Double, optimalPricePerKg: Double?, status: String) -> Unit
) {
    var number by remember { mutableStateOf(editing?.number ?: "") }
    var formationDate by remember { mutableStateOf(editing?.formationDate ?: "") }
    var rawQuantityKg by remember { mutableStateOf(editing?.rawQuantityKg?.toString() ?: "") }
    var outputPercent by remember { mutableStateOf(editing?.outputPercent?.toString() ?: "") }
    var costPrice by remember { mutableStateOf(editing?.costPrice?.toString() ?: "") }
    var optimalPricePerKg by remember { mutableStateOf(editing?.optimalPricePerKg?.toString() ?: "") }
    var status by remember { mutableStateOf(editing?.status ?: Batch.STATUS_ACTIVE) }

    var numberError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var outputError by remember { mutableStateOf(false) }
    var costError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новая партия" else "Редактировать партию") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(value = number, onValueChange = { number = it; numberError = false },
                    label = "Номер партии *", isError = numberError, errorText = "Обязательное поле")
                AppTextField(value = formationDate, onValueChange = { formationDate = it; dateError = false },
                    label = "Дата формирования * (ГГГГ-ММ-ДД)", isError = dateError,
                    errorText = "Обязательное поле")
                AppTextField(value = rawQuantityKg, onValueChange = { rawQuantityKg = it; quantityError = false },
                    label = "Сырьё (кг) *", isError = quantityError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = outputPercent, onValueChange = { outputPercent = it; outputError = false },
                    label = "Выход (%) *", isError = outputError, errorText = "Введите 1-100",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                AppTextField(value = costPrice, onValueChange = { costPrice = it; costError = false },
                    label = "Себестоимость (₽) *", isError = costError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = optimalPricePerKg, onValueChange = { optimalPricePerKg = it },
                    label = "Оптимальная цена ₽/кг (опц.)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
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
                numberError = number.isBlank()
                dateError = formationDate.isBlank()
                quantityError = rawQuantityKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                val pct = outputPercent.toIntOrNull()
                outputError = pct == null || pct !in 1..100
                costError = costPrice.toDoubleOrNull()?.let { it <= 0 } ?: true
                if (!numberError && !dateError && !quantityError && !outputError && !costError) {
                    onSave(
                        number, formationDate, rawQuantityKg.toDouble(),
                        outputPercent.toInt(), costPrice.toDouble(),
                        optimalPricePerKg.toDoubleOrNull(), status
                    )
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
