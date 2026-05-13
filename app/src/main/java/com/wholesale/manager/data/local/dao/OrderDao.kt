package com.wholesale.manager.data.local.dao

import androidx.room.*
import com.wholesale.manager.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE isDeleted = 0 ORDER BY creationDate DESC")
    fun getAllNotDeleted(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OrderEntity)

    @Update
    suspend fun update(entity: OrderEntity)

    @Query("UPDATE orders SET isDeleted = 1, lastModified = :timestamp WHERE id = :id")
    suspend fun softDelete(id: String, timestamp: String)
}
