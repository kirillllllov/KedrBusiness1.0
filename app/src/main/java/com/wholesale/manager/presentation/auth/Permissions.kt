package com.wholesale.manager.presentation.auth

import com.wholesale.manager.domain.model.User

object Permissions {
    fun canAddPurchase(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canViewPurchases(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canEditPurchase(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canDeletePurchase(role: String) = role == User.ROLE_DIRECTOR

    fun canCreateBatch(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canEditBatch(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canDeleteBatch(role: String) = role == User.ROLE_DIRECTOR
    fun canViewBatches(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR

    fun canAddExpense(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canEditExpense(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canDeleteExpense(role: String) = role == User.ROLE_DIRECTOR
    fun canViewExpenses(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR

    fun canCreateOrder(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canEditOrder(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canCancelOrder(role: String) = role == User.ROLE_ADMIN || role == User.ROLE_DIRECTOR
    fun canUpdateOrderStatus(role: String) = true
    fun canViewOrders(role: String) = true

    fun canManageUsers(role: String) = role == User.ROLE_DIRECTOR
}
