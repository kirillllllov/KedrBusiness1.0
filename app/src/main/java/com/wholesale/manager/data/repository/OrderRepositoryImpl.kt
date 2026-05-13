package com.wholesale.manager.data.repository

import com.wholesale.manager.data.local.dao.OrderDao
import com.wholesale.manager.data.local.entity.OrderEntity
import com.wholesale.manager.domain.model.Order
import com.wholesale.manager.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class OrderRepositoryImpl(private val dao: OrderDao) : OrderRepository {

    override fun getAll(): Flow<List<Order>> =
        dao.getAllNotDeleted().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): Order? = dao.getById(id)?.toDomain()

    override suspend fun create(order: Order) = dao.insert(order.toEntity())

    override suspend fun update(order: Order) = dao.update(order.toEntity())

    override suspend fun delete(id: String) {
        dao.softDelete(id, Instant.now().toString())
    }

    private fun OrderEntity.toDomain() = Order(
        id = id, serverId = serverId, lastModified = lastModified, isDeleted = isDeleted,
        customerName = customerName, customerPhone = customerPhone,
        customerAddress = customerAddress, batchId = batchId,
        quantityKg = quantityKg, pricePerKg = pricePerKg, totalAmount = totalAmount,
        creationDate = creationDate, shipmentDate = shipmentDate,
        deliveryMethod = deliveryMethod, status = status
    )

    private fun Order.toEntity() = OrderEntity(
        id = id, serverId = serverId, lastModified = lastModified, isDeleted = isDeleted,
        customerName = customerName, customerPhone = customerPhone,
        customerAddress = customerAddress, batchId = batchId,
        quantityKg = quantityKg, pricePerKg = pricePerKg, totalAmount = totalAmount,
        creationDate = creationDate, shipmentDate = shipmentDate,
        deliveryMethod = deliveryMethod, status = status
    )
}
