package com.wholesale.manager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wholesale.manager.data.local.dao.*
import com.wholesale.manager.data.local.entity.*
import com.wholesale.manager.domain.model.User
import java.security.MessageDigest
import java.util.UUID

@Database(
    entities = [
        BatchEntity::class,
        PurchasedRawEntity::class,
        OrderEntity::class,
        ExpenseEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun batchDao(): BatchDao
    abstract fun purchasedRawDao(): PurchasedRawDao
    abstract fun orderDao(): OrderDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE purchased_raws ADD COLUMN number INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE batches ADD COLUMN purchaseId TEXT")
                db.execSQL("ALTER TABLE batches ADD COLUMN outputKg REAL NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE expenses ADD COLUMN purchaseId TEXT")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS users (
                        id TEXT NOT NULL PRIMARY KEY,
                        username TEXT NOT NULL,
                        passwordHash TEXT NOT NULL,
                        role TEXT NOT NULL,
                        displayName TEXT NOT NULL
                    )"""
                )
                val hash = hashPassword("director123")
                val id = UUID.randomUUID().toString()
                db.execSQL(
                    "INSERT OR IGNORE INTO users (id, username, passwordHash, role, displayName) " +
                            "VALUES ('$id', 'director', '$hash', '${User.ROLE_DIRECTOR}', 'Директор')"
                )
            }
        }

        private val DB_CREATE_CALLBACK = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val hash = hashPassword("director123")
                val id = UUID.randomUUID().toString()
                db.execSQL(
                    "INSERT OR IGNORE INTO users (id, username, passwordHash, role, displayName) " +
                            "VALUES ('$id', 'director', '$hash', '${User.ROLE_DIRECTOR}', 'Директор')"
                )
            }
        }

        fun hashPassword(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wholesale_manager.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .addCallback(DB_CREATE_CALLBACK)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
