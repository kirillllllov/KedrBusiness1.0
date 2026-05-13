package com.wholesale.manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val serverId: String? = null,
    val lastModified: String,
    val isDeleted: Boolean = false,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String? = null,
    val batchId: String,
    val quantityKg: Double,
    val pricePerKg: Double,
    val totalAmount: Double,
    val creationDate: String,
    val shipmentDate: String? = null,
    val deliveryMethod: String,
    val status: String
)
