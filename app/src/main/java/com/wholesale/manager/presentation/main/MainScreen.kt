package com.wholesale.manager.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.wholesale.manager.di.AppModule
import com.wholesale.manager.presentation.main.tabs.batches.BatchesTab
import com.wholesale.manager.presentation.main.tabs.batches.BatchesViewModel
import com.wholesale.manager.presentation.main.tabs.batches.BatchesViewModelFactory
import com.wholesale.manager.presentation.main.tabs.expenses.ExpensesTab
import com.wholesale.manager.presentation.main.tabs.expenses.ExpensesViewModel
import com.wholesale.manager.presentation.main.tabs.expenses.ExpensesViewModelFactory
import com.wholesale.manager.presentation.main.tabs.orders.OrdersTab
import com.wholesale.manager.presentation.main.tabs.orders.OrdersViewModel
import com.wholesale.manager.presentation.main.tabs.orders.OrdersViewModelFactory
import com.wholesale.manager.presentation.main.tabs.purchases.PurchasesTab
import com.wholesale.manager.presentation.main.tabs.purchases.PurchasesViewModel
import com.wholesale.manager.presentation.main.tabs.purchases.PurchasesViewModelFactory

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val purchasesViewModel: PurchasesViewModel = viewModel(
        factory = PurchasesViewModelFactory(
            AppModule.providePurchaseUseCases(context),
            AppModule.provideBatchUseCases(context)
        )
    )
    val batchesViewModel: BatchesViewModel = viewModel(
        factory = BatchesViewModelFactory(
            AppModule.provideBatchUseCases(context),
            AppModule.providePurchaseUseCases(context),
            AppModule.provideExpenseUseCases(context)
        )
    )
    val expensesViewModel: ExpensesViewModel = viewModel(
        factory = ExpensesViewModelFactory(
            AppModule.provideExpenseUseCases(context),
            AppModule.providePurchaseUseCases(context)
        )
    )
    val ordersViewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(
            AppModule.provideOrderUseCases(context),
            AppModule.provideBatchUseCases(context)
        )
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBar {
                BottomNavItem.all.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Purchases.route
            ) {
                composable(BottomNavItem.Purchases.route) {
                    PurchasesTab(viewModel = purchasesViewModel)
                }
                composable(BottomNavItem.Batches.route) {
                    BatchesTab(viewModel = batchesViewModel)
                }
                composable(BottomNavItem.Expenses.route) {
                    ExpensesTab(viewModel = expensesViewModel)
                }
                composable(BottomNavItem.Orders.route) {
                    OrdersTab(viewModel = ordersViewModel)
                }
            }
        }
    }
}
