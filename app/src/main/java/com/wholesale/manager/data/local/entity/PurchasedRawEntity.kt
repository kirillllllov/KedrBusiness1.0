package com.wholesale.manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchased_raws")
data class PurchasedRawEntity(
    @PrimaryKey val id: String,
    val number: Int = 0,
    val serverId: String? = null,
    val lastModified: String,
    val isDeleted: Boolean = false,
    val type: String,
    val quantityKg: Double,
    val purchasePriceTotal: Double,
    val pricePerKg: Double,
    val supplierName: String,
    val purchaseDate: String,
    val status: String,
    val batchId: String? = null
)
