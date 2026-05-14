package com.wholesale.manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batches")
data class BatchEntity(
    @PrimaryKey val id: String,
    val serverId: String? = null,
    val number: String,
    val formationDate: String,
    val purchaseId: String? = null,
    val rawQuantityKg: Double,
    val outputKg: Double = 0.0,
    val outputPercent: Int,
    val costPrice: Double,
    val optimalPricePerKg: Double? = null,
    val status: String,
    val lastModified: String,
    val isDeleted: Boolean = false
)
