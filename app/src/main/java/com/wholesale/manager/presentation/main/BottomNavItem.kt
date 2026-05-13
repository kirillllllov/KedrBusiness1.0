package com.wholesale.manager.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Purchases : BottomNavItem("purchases", "Закупки", Icons.Filled.ShoppingCart)
    object Batches : BottomNavItem("batches", "Партии", Icons.Filled.Inventory)
    object Expenses : BottomNavItem("expenses", "Расходы", Icons.Filled.AttachMoney)
    object Orders : BottomNavItem("orders", "Заказы", Icons.Filled.Receipt)

    companion object {
        val all = listOf(Purchases, Batches, Expenses, Orders)
    }
}
