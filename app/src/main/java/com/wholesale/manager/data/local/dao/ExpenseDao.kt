package com.wholesale.manager.data.local.dao

import androidx.room.*
import com.wholesale.manager.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllNotDeleted(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExpenseEntity)

    @Update
    suspend fun update(entity: ExpenseEntity)

    @Query("UPDATE expenses SET isDeleted = 1, lastModified = :timestamp WHERE id = :id")
    suspend fun softDelete(id: String, timestamp: String)
}
