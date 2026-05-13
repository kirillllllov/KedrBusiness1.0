package com.wholesale.manager.domain.model

data class Order(
    val id: String,
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
) {
    companion object {
        const val STATUS_NEW = "NEW"
        const val STATUS_CONFIRMED = "CONFIRMED"
        const val STATUS_SHIPPED = "SHIPPED"
        const val STATUS_DELIVERED = "DELIVERED"
        const val STATUS_CANCELLED = "CANCELLED"

        const val DELIVERY_PICKUP = "PICKUP"
        const val DELIVERY_COURIER = "COURIER"
        const val DELIVERY_TRANSPORT = "TRANSPORT"
    }
}
