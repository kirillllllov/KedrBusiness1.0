package com.wholesale.manager.domain.model

data class Batch(
    val id: String,
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
) {
    companion object {
        const val STATUS_ACTIVE = "ACTIVE"
        const val STATUS_SOLD_OUT = "SOLD_OUT"
    }
}
