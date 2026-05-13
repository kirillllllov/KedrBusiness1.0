package com.wholesale.manager.data.local.dao

import androidx.room.*
import com.wholesale.manager.data.local.entity.PurchasedRawEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedRawDao {
    @Query("SELECT * FROM purchased_raws WHERE isDeleted = 0 ORDER BY purchaseDate DESC")
    fun getAllNotDeleted(): Flow<List<PurchasedRawEntity>>

    @Query("SELECT * FROM purchased_raws WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): PurchasedRawEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PurchasedRawEntity)

    @Update
    suspend fun update(entity: PurchasedRawEntity)

    @Query("UPDATE purchased_raws SET isDeleted = 1, lastModified = :timestamp WHERE id = :id")
    suspend fun softDelete(id: String, timestamp: String)
}
