package com.wholesale.manager.presentation.main.tabs.purchases

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField

@Composable
fun AddEditPurchaseDialog(
    editing: PurchasedRaw?,
    onDismiss: () -> Unit,
    onSave: (type: String, quantityKg: Double, purchasePriceTotal: Double,
             supplierName: String, purchaseDate: String, status: String, batchId: String?) -> Unit
) {
    var type by remember { mutableStateOf(editing?.type ?: "") }
    var quantityKg by remember { mutableStateOf(editing?.quantityKg?.toString() ?: "") }
    var purchasePriceTotal by remember { mutableStateOf(editing?.purchasePriceTotal?.toString() ?: "") }
    var supplierName by remember { mutableStateOf(editing?.supplierName ?: "") }
    var purchaseDate by remember { mutableStateOf(editing?.purchaseDate ?: "") }
    var status by remember { mutableStateOf(editing?.status ?: PurchasedRaw.STATUS_PENDING) }
    var batchId by remember { mutableStateOf(editing?.batchId ?: "") }

    var typeError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var supplierError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новая закупка" else "Редактировать закупку") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(value = type, onValueChange = { type = it; typeError = false },
                    label = "Вид сырья *", isError = typeError,
                    errorText = "Обязательное поле")
                AppTextField(value = supplierName, onValueChange = { supplierName = it; supplierError = false },
                    label = "Поставщик *", isError = supplierError,
                    errorText = "Обязательное поле")
                AppTextField(value = quantityKg, onValueChange = { quantityKg = it; quantityError = false },
                    label = "Количество (кг) *", isError = quantityError,
                    errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = purchasePriceTotal, onValueChange = { purchasePriceTotal = it; priceError = false },
                    label = "Сумма закупки (₽) *", isError = priceError,
                    errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = purchaseDate, onValueChange = { purchaseDate = it; dateError = false },
                    label = "Дата закупки * (ГГГГ-ММ-ДД)", isError = dateError,
                    errorText = "Обязательное поле")
                AppDropdown(
                    label = "Статус",
                    selected = status,
                    options = listOf(
                        PurchasedRaw.STATUS_PENDING to "Ожидает",
                        PurchasedRaw.STATUS_IN_BATCH to "В партии",
                        PurchasedRaw.STATUS_PROCESSED to "Обработано"
                    ),
                    onSelected = { status = it }
                )
                AppTextField(value = batchId, onValueChange = { batchId = it },
                    label = "ID партии (опционально)")
            }
        },
        confirmButton = {
            Button(onClick = {
                typeError = type.isBlank()
                supplierError = supplierName.isBlank()
                quantityError = quantityKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                priceError = purchasePriceTotal.toDoubleOrNull()?.let { it <= 0 } ?: true
                dateError = purchaseDate.isBlank()
                if (!typeError && !supplierError && !quantityError && !priceError && !dateError) {
                    onSave(type, quantityKg.toDouble(), purchasePriceTotal.toDouble(),
                        supplierName, purchaseDate, status, batchId.ifBlank { null })
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
