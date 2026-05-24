package com.wholesale.manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val serverId: String? = null,
    val lastModified: String,
    val isDeleted: Boolean = false,
    val type: String,
    val amount: Double,
    val date: String,
    val description: String? = null,
    val batchIdsJson: String = "[]",
    val purchaseId: String? = null,
    val batchId: String? = null
)
