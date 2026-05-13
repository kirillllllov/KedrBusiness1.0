package com.wholesale.manager.presentation.main.tabs.orders

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
import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField

@Composable
fun AddEditOrderDialog(
    editing: Order?,
    availableBatches: List<Batch>,
    onDismiss: () -> Unit,
    onSave: (customerName: String, customerPhone: String, customerAddress: String?,
             batchId: String, quantityKg: Double, pricePerKg: Double,
             shipmentDate: String?, deliveryMethod: String, status: String) -> Unit
) {
    var customerName by remember { mutableStateOf(editing?.customerName ?: "") }
    var customerPhone by remember { mutableStateOf(editing?.customerPhone ?: "") }
    var customerAddress by remember { mutableStateOf(editing?.customerAddress ?: "") }
    var batchId by remember { mutableStateOf(editing?.batchId ?: availableBatches.firstOrNull()?.id ?: "") }
    var quantityKg by remember { mutableStateOf(editing?.quantityKg?.toString() ?: "") }
    var pricePerKg by remember { mutableStateOf(editing?.pricePerKg?.toString() ?: "") }
    var shipmentDate by remember { mutableStateOf(editing?.shipmentDate ?: "") }
    var deliveryMethod by remember { mutableStateOf(editing?.deliveryMethod ?: Order.DELIVERY_PICKUP) }
    var status by remember { mutableStateOf(editing?.status ?: Order.STATUS_NEW) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новый заказ" else "Редактировать заказ") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(value = customerName, onValueChange = { customerName = it; nameError = false },
                    label = "Клиент *", isError = nameError, errorText = "Обязательное поле")
                AppTextField(value = customerPhone, onValueChange = { customerPhone = it; phoneError = false },
                    label = "Телефон *", isError = phoneError, errorText = "Обязательное поле",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                AppTextField(value = customerAddress, onValueChange = { customerAddress = it },
                    label = "Адрес (опц.)")
                if (availableBatches.isNotEmpty()) {
                    AppDropdown(
                        label = "Партия",
                        selected = batchId,
                        options = availableBatches.map { it.id to "Партия №${it.number}" },
                        onSelected = { batchId = it }
                    )
                } else {
                    AppTextField(value = batchId, onValueChange = { batchId = it },
                        label = "ID партии")
                }
                AppTextField(value = quantityKg, onValueChange = { quantityKg = it; quantityError = false },
                    label = "Кол-во (кг) *", isError = quantityError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = pricePerKg, onValueChange = { pricePerKg = it; priceError = false },
                    label = "Цена за кг (₽) *", isError = priceError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                AppTextField(value = shipmentDate, onValueChange = { shipmentDate = it },
                    label = "Дата отгрузки (ГГГГ-ММ-ДД, опц.)")
                AppDropdown(
                    label = "Доставка",
                    selected = deliveryMethod,
                    options = listOf(
                        Order.DELIVERY_PICKUP to "Самовывоз",
                        Order.DELIVERY_COURIER to "Курьер",
                        Order.DELIVERY_TRANSPORT to "Транспортная"
                    ),
                    onSelected = { deliveryMethod = it }
                )
                AppDropdown(
                    label = "Статус",
                    selected = status,
                    options = listOf(
                        Order.STATUS_NEW to "Новый",
                        Order.STATUS_CONFIRMED to "Подтверждён",
                        Order.STATUS_SHIPPED to "Отправлен",
                        Order.STATUS_DELIVERED to "Доставлен",
                        Order.STATUS_CANCELLED to "Отменён"
                    ),
                    onSelected = { status = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                nameError = customerName.isBlank()
                phoneError = customerPhone.isBlank()
                quantityError = quantityKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                priceError = pricePerKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                if (!nameError && !phoneError && !quantityError && !priceError) {
                    onSave(
                        customerName, customerPhone, customerAddress.ifBlank { null },
                        batchId, quantityKg.toDouble(), pricePerKg.toDouble(),
                        shipmentDate.ifBlank { null }, deliveryMethod, status
                    )
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
