package com.wholesale.manager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wholesale.manager.data.local.dao.*
import com.wholesale.manager.data.local.entity.*

@Database(
    entities = [
        BatchEntity::class,
        PurchasedRawEntity::class,
        OrderEntity::class,
        ExpenseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun batchDao(): BatchDao
    abstract fun purchasedRawDao(): PurchasedRawDao
    abstract fun orderDao(): OrderDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wholesale_manager.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
