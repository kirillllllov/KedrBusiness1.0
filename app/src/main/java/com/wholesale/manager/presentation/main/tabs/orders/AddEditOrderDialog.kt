package com.wholesale.manager.presentation.main.tabs.orders

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
import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.presentation.common.AppDropdown
import com.wholesale.manager.presentation.common.AppTextField
import com.wholesale.manager.presentation.common.DatePickerField
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditOrderDialog(
    editing: Order?,
    availableBatches: List<Batch>,
    onDismiss: () -> Unit,
    onSave: (customerName: String, customerPhone: String, customerAddress: String?,
             batchId: String, quantityKg: Double, pricePerKg: Double,
             shipmentDate: String?, deliveryMethod: String, status: String) -> Unit
) {
    val today = LocalDate.now().toString()
    var customerName by remember { mutableStateOf(editing?.customerName ?: "") }
    var customerPhone by remember { mutableStateOf(editing?.customerPhone ?: "") }
    var customerAddress by remember { mutableStateOf(editing?.customerAddress ?: "") }
    var batchId by remember { mutableStateOf(editing?.batchId ?: availableBatches.firstOrNull()?.id ?: "") }
    var batchDropdownExpanded by remember { mutableStateOf(false) }
    var quantityKg by remember { mutableStateOf(editing?.quantityKg?.toString() ?: "") }
    var pricePerKg by remember { mutableStateOf(editing?.pricePerKg?.toString() ?: "") }
    var shipmentDate by remember { mutableStateOf(editing?.shipmentDate ?: today) }
    var deliveryMethod by remember { mutableStateOf(editing?.deliveryMethod ?: Order.DELIVERY_PICKUP) }
    var status by remember { mutableStateOf(editing?.status ?: Order.STATUS_NEW) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    val selectedBatch = availableBatches.find { it.id == batchId }
    val availableKg = selectedBatch?.outputKg ?: 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing == null) "Новый заказ" else "Редактировать заказ") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    value = customerName,
                    onValueChange = { customerName = it; nameError = false },
                    label = "Клиент *", isError = nameError, errorText = "Обязательное поле"
                )
                AppTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it; phoneError = false },
                    label = "Телефон *", isError = phoneError, errorText = "Обязательное поле",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                AppTextField(
                    value = customerAddress,
                    onValueChange = { customerAddress = it },
                    label = "Адрес (опц.)"
                )

                ExposedDropdownMenuBox(
                    expanded = batchDropdownExpanded,
                    onExpandedChange = { batchDropdownExpanded = it }
                ) {
                    val batchDisplay = selectedBatch?.let {
                        "Партия №${it.number} · ${it.formationDate} · ${batchStatusLabel(it.status)}"
                    } ?: if (batchId.isBlank()) "Не выбрана" else batchId

                    OutlinedTextField(
                        value = batchDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Партия *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = batchDropdownExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = batchDropdownExpanded,
                        onDismissRequest = { batchDropdownExpanded = false }
                    ) {
                        availableBatches.forEach { batch ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            "Партия №${batch.number} · ${batchStatusLabel(batch.status)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "${batch.formationDate} · Выход: ${batch.outputKg} кг · ${batch.outputPercent}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        batch.optimalPricePerKg?.let { so ->
                                            Text(
                                                "Оптим. стоим.: ${String.format("%.2f", so)} ₽/кг",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    batchId = batch.id
                                    if (editing == null) {
                                        batch.optimalPricePerKg?.let {
                                            pricePerKg = String.format("%.2f", it)
                                        }
                                    }
                                    batchDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (availableKg > 0) {
                    Text(
                        "Доступно: ${String.format("%.1f", availableKg)} кг",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                AppTextField(
                    value = quantityKg,
                    onValueChange = { quantityKg = it; quantityError = false },
                    label = "Кол-во (кг) *", isError = quantityError,
                    errorText = if (quantityError) {
                        if (selectedBatch != null && quantityKg.toDoubleOrNull()?.let { it > availableKg } == true) {
                            "Нельзя больше доступного остатка"
                        } else {
                            "Введите число > 0"
                        }
                    } else "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                AppTextField(
                    value = pricePerKg,
                    onValueChange = { pricePerKg = it; priceError = false },
                    label = "Цена за кг (₽) *", isError = priceError, errorText = "Введите число > 0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                DatePickerField(
                    label = "Дата отгрузки",
                    value = shipmentDate,
                    onValueChange = { shipmentDate = it }
                )

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
                        Order.STATUS_COMPLETED to "Выполнен",
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
                val quantityValue = quantityKg.toDoubleOrNull()
                quantityError = quantityValue?.let { it <= 0 || it > availableKg || availableKg <= 0 } ?: true
                priceError = pricePerKg.toDoubleOrNull()?.let { it <= 0 } ?: true
                if (!nameError && !phoneError && !quantityError && !priceError) {
                    onSave(
                        customerName, customerPhone, customerAddress.ifBlank { null },
                        batchId, quantityValue ?: 0.0, pricePerKg.toDouble(),
                        shipmentDate.ifBlank { null }, deliveryMethod, status
                    )
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
