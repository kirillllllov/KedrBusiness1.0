package com.wholesale.manager.domain.model

data class Expense(
    val id: String,
    val serverId: String? = null,
    val lastModified: String,
    val isDeleted: Boolean = false,
    val type: String,
    val amount: Double,
    val date: String,
    val description: String? = null,
    val purchaseId: String? = null,
    val batchId: String? = null
) {
    companion object {
        const val TYPE_TRANSPORT = "TRANSPORT"
        const val TYPE_PROCESSING = "PROCESSING"
        const val TYPE_STORAGE = "STORAGE"
        const val TYPE_SALARY = "SALARY"
        const val TYPE_UTILITY = "UTILITY"
        const val TYPE_EQUIPMENT = "EQUIPMENT"
        const val TYPE_OTHER = "OTHER"
    }
}
