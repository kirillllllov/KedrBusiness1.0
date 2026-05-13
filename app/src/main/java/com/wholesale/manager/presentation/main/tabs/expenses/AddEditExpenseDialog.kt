package com.wholesale.manager.presentation.main.tabs.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField

@Composable
fun AddEditExpenseDialog(
    editing: Expense?,
    onDismiss: () -> Unit,
    onSave: (type: String, amount: Double, date: String, description: String?, batchIds: List<String>) -> Unit
) {
    var type by remember { mutableStateOf(editing?.type ?: Expense.TYPE_OTHER) }
    var amount by remember { mutableStateOf(editing?.amount?.toString() ?: "") }
    var date by remember { mutableStateOf(editing?.date ?: "") }
    var description by remember { mutableStateOf(editing?.description ?: "") }
    var batchIdsText by remember { mutableStateOf(editing?.batchIds?.joinToString(", ") ?: "") }

    var amountError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новый расход" else "Редактировать расход") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppDropdown(
                    label = "Тип расхода",
                    selected = type,
                    options = listOf(
                        Expense.TYPE_TRANSPORT to "Транспорт",
                        Expense.TYPE_SALARY to "Зарплата",
                        Expense.TYPE_UTILITY to "Коммунальные",
                        Expense.TYPE_EQUIPMENT to "Оборудование",
                        Expense.TYPE_OTHER to "Прочее"
                    ),
                    onSelected = { type = it }
                )
                AppTextField(value = amount, onValueChange = { amount = it; amountError = false },
                    label = "Сумма (₽) *", isError = amountError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = date, onValueChange = { date = it; dateError = false },
                    label = "Дата * (ГГГГ-ММ-ДД)", isError = dateError, errorText = "Обязательное поле")
                AppTextField(value = description, onValueChange = { description = it },
                    label = "Описание (опц.)", maxLines = 3)
                AppTextField(value = batchIdsText, onValueChange = { batchIdsText = it },
                    label = "ID партий (через запятую)")
            }
        },
        confirmButton = {
            Button(onClick = {
                amountError = amount.toDoubleOrNull()?.let { it <= 0 } ?: true
                dateError = date.isBlank()
                if (!amountError && !dateError) {
                    val ids = batchIdsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    onSave(type, amount.toDouble(), date, description.ifBlank { null }, ids)
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
