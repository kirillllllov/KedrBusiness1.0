package com.wholesale.manager.data.local.dao

import androidx.room.*
import com.wholesale.manager.data.local.entity.BatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchDao {
    @Query("SELECT * FROM batches WHERE isDeleted = 0 ORDER BY formationDate DESC")
    fun getAllNotDeleted(): Flow<List<BatchEntity>>

    @Query("SELECT * FROM batches WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): BatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BatchEntity)

    @Update
    suspend fun update(entity: BatchEntity)

    @Query("UPDATE batches SET isDeleted = 1, lastModified = :timestamp WHERE id = :id")
    suspend fun softDelete(id: String, timestamp: String)
}
