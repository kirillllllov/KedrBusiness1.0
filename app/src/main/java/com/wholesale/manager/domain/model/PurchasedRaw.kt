package com.wholesale.manager.domain.model

data class PurchasedRaw(
    val id: String,
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
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_IN_BATCH = "IN_BATCH"
        const val STATUS_PROCESSED = "PROCESSED"
    }
}
