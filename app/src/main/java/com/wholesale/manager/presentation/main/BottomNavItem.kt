package com.wholesale.manager.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.wholesale.manager.domain.model.User

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Purchases : BottomNavItem("purchases", "Закупки", Icons.Filled.ShoppingCart)
    object Batches : BottomNavItem("batches", "Партии", Icons.Filled.Inventory)
    object Expenses : BottomNavItem("expenses", "Расходы", Icons.Filled.AttachMoney)
    object Orders : BottomNavItem("orders", "Заказы", Icons.Filled.Receipt)
    object Users : BottomNavItem("users", "Кадры", Icons.Filled.People)

    companion object {
        fun forRole(role: String): List<BottomNavItem> = when (role) {
            User.ROLE_DIRECTOR -> listOf(Purchases, Batches, Expenses, Orders, Users)
            User.ROLE_ADMIN -> listOf(Purchases, Batches, Expenses, Orders)
            User.ROLE_EXECUTOR -> listOf(Orders)
            else -> listOf(Orders)
        }
    }
}
