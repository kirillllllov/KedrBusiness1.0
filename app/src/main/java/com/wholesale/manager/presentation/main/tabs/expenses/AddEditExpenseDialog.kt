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
import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.model.Expense
import com.wholesale.manager.presentation.common.AppTextField
import com.wholesale.manager.presentation.common.DatePickerField

private const val CUSTOM_TYPE_KEY = "__CUSTOM__"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseDialog(
    editing: Expense?,
    availableBatches: List<Batch>,
    onDismiss: () -> Unit,
    onSave: (type: String, amount: Double, date: String, description: String?, batchIds: List<String>) -> Unit
) {
    val predefinedTypes = listOf(
        Expense.TYPE_TRANSPORT to "Транспорт",
        Expense.TYPE_SALARY to "Зарплата",
        Expense.TYPE_UTILITY to "Коммунальные",
        Expense.TYPE_EQUIPMENT to "Оборудование",
        Expense.TYPE_OTHER to "Прочее",
        CUSTOM_TYPE_KEY to "Свой тип..."
    )

    val editingTypeIsCustom = editing?.type != null &&
            predefinedTypes.none { it.first == editing.type } &&
            editing.type != CUSTOM_TYPE_KEY

    var selectedTypeKey by remember {
        mutableStateOf(
            if (editingTypeIsCustom) CUSTOM_TYPE_KEY
            else editing?.type ?: Expense.TYPE_OTHER
        )
    }
    var customTypeText by remember { mutableStateOf(if (editingTypeIsCustom) editing!!.type else "") }
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    var amount by remember { mutableStateOf(editing?.amount?.toString() ?: "") }
    var date by remember { mutableStateOf(editing?.date ?: "") }
    var description by remember { mutableStateOf(editing?.description ?: "") }

    var selectedBatchId by remember {
        mutableStateOf(editing?.batchIds?.firstOrNull() ?: "")
    }
    var batchDropdownExpanded by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var customTypeError by remember { mutableStateOf(false) }

    val displayTypeName = if (selectedTypeKey == CUSTOM_TYPE_KEY && customTypeText.isNotBlank())
        customTypeText
    else predefinedTypes.firstOrNull { it.first == selectedTypeKey }?.second ?: selectedTypeKey

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новый расход" else "Редактировать расход") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = typeDropdownExpanded,
                    onExpandedChange = { typeDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = displayTypeName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Тип расхода") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = typeDropdownExpanded,
                        onDismissRequest = { typeDropdownExpanded = false }
                    ) {
                        predefinedTypes.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedTypeKey = key
                                    typeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedTypeKey == CUSTOM_TYPE_KEY) {
                    AppTextField(
                        value = customTypeText,
                        onValueChange = { customTypeText = it; customTypeError = false },
                        label = "Введите название типа *",
                        isError = customTypeError,
                        errorText = "Обязательное поле"
                    )
                }

                AppTextField(
                    value = amount,
                    onValueChange = { amount = it; amountError = false },
                    label = "Сумма (₽) *", isError = amountError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                DatePickerField(
                    label = "Дата *",
                    value = date,
                    onValueChange = { date = it; dateError = false },
                    isError = dateError,
                    errorText = "Обязательное поле"
                )

                AppTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Описание (опц.)", maxLines = 3
                )

                ExposedDropdownMenuBox(
                    expanded = batchDropdownExpanded,
                    onExpandedChange = { batchDropdownExpanded = it }
                ) {
                    val batchDisplayName = availableBatches.find { it.id == selectedBatchId }
                        ?.let { "Партия №${it.number} (${it.formationDate})" }
                        ?: if (selectedBatchId.isBlank()) "Не выбрана" else selectedBatchId

                    OutlinedTextField(
                        value = batchDisplayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Партия (опц.)") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = batchDropdownExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = batchDropdownExpanded,
                        onDismissRequest = { batchDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Не выбрана") },
                            onClick = {
                                selectedBatchId = ""
                                batchDropdownExpanded = false
                            }
                        )
                        availableBatches.forEach { batch ->
                            DropdownMenuItem(
                                text = {
                                    Text("Партия №${batch.number} · ${batch.formationDate} · ${batchStatusLabel(batch.status)}")
                                },
                                onClick = {
                                    selectedBatchId = batch.id
                                    batchDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                amountError = amount.toDoubleOrNull()?.let { it <= 0 } ?: true
                dateError = date.isBlank()
                customTypeError = selectedTypeKey == CUSTOM_TYPE_KEY && customTypeText.isBlank()
                if (!amountError && !dateError && !customTypeError) {
                    val finalType = if (selectedTypeKey == CUSTOM_TYPE_KEY) customTypeText else selectedTypeKey
                    val ids = if (selectedBatchId.isNotBlank()) listOf(selectedBatchId) else emptyList()
                    onSave(finalType, amount.toDouble(), date, description.ifBlank { null }, ids)
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

private fun batchStatusLabel(status: String) = when (status) {
    "ACTIVE" -> "Активна"
    "SOLD_OUT" -> "Продана"
    else -> status
}
