package com.wholesale.manager.di

import android.content.Context
import com.wholesale.manager.data.local.AppDatabase
import com.wholesale.manager.data.repository.*
import com.wholesale.manager.domain.repository.*
import com.wholesale.manager.domain.usecase.batch.*
import com.wholesale.manager.domain.usecase.purchase.*
import com.wholesale.manager.domain.usecase.order.*
import com.wholesale.manager.domain.usecase.expense.*
import com.wholesale.manager.domain.usecase.user.*

object AppModule {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return db ?: AppDatabase.getInstance(context).also { db = it }
    }

    fun provideBatchRepository(context: Context): BatchRepository =
        BatchRepositoryImpl(getDatabase(context).batchDao())

    fun providePurchasedRawRepository(context: Context): PurchasedRawRepository =
        PurchasedRawRepositoryImpl(getDatabase(context).purchasedRawDao())

    fun provideOrderRepository(context: Context): OrderRepository =
        OrderRepositoryImpl(getDatabase(context).orderDao())

    fun provideExpenseRepository(context: Context): ExpenseRepository =
        ExpenseRepositoryImpl(getDatabase(context).expenseDao())

    fun provideUserRepository(context: Context): UserRepository =
        UserRepositoryImpl(getDatabase(context).userDao())

    fun provideBatchUseCases(context: Context): BatchUseCases {
        val repo = provideBatchRepository(context)
        return BatchUseCases(
            getAll = GetAllBatchesUseCase(repo),
            getById = GetBatchByIdUseCase(repo),
            create = CreateBatchUseCase(repo),
            update = UpdateBatchUseCase(repo),
            delete = DeleteBatchUseCase(repo)
        )
    }

    fun providePurchaseUseCases(context: Context): PurchaseUseCases {
        val repo = providePurchasedRawRepository(context)
        return PurchaseUseCases(
            getAll = GetAllPurchasesUseCase(repo),
            getById = GetPurchaseByIdUseCase(repo),
            create = CreatePurchaseUseCase(repo),
            update = UpdatePurchaseUseCase(repo),
            delete = DeletePurchaseUseCase(repo)
        )
    }

    fun provideOrderUseCases(context: Context): OrderUseCases {
        val repo = provideOrderRepository(context)
        return OrderUseCases(
            getAll = GetAllOrdersUseCase(repo),
            getById = GetOrderByIdUseCase(repo),
            create = CreateOrderUseCase(repo),
            update = UpdateOrderUseCase(repo),
            delete = DeleteOrderUseCase(repo)
        )
    }

    fun provideExpenseUseCases(context: Context): ExpenseUseCases {
        val repo = provideExpenseRepository(context)
        return ExpenseUseCases(
            getAll = GetAllExpensesUseCase(repo),
            getById = GetExpenseByIdUseCase(repo),
            create = CreateExpenseUseCase(repo),
            update = UpdateExpenseUseCase(repo),
            delete = DeleteExpenseUseCase(repo)
        )
    }

    fun provideUserUseCases(context: Context): UserUseCases {
        val repo = provideUserRepository(context)
        return UserUseCases(
            getAll = GetAllUsersUseCase(repo),
            getByUsername = GetUserByUsernameUseCase(repo),
            create = CreateUserUseCase(repo),
            update = UpdateUserUseCase(repo),
            delete = DeleteUserUseCase(repo),
            countDirectors = CountDirectorsUseCase(repo)
        )
    }
}

data class BatchUseCases(
    val getAll: GetAllBatchesUseCase,
    val getById: GetBatchByIdUseCase,
    val create: CreateBatchUseCase,
    val update: UpdateBatchUseCase,
    val delete: DeleteBatchUseCase
)

data class PurchaseUseCases(
    val getAll: GetAllPurchasesUseCase,
    val getById: GetPurchaseByIdUseCase,
    val create: CreatePurchaseUseCase,
    val update: UpdatePurchaseUseCase,
    val delete: DeletePurchaseUseCase
)

data class OrderUseCases(
    val getAll: GetAllOrdersUseCase,
    val getById: GetOrderByIdUseCase,
    val create: CreateOrderUseCase,
    val update: UpdateOrderUseCase,
    val delete: DeleteOrderUseCase
)

data class ExpenseUseCases(
    val getAll: GetAllExpensesUseCase,
    val getById: GetExpenseByIdUseCase,
    val create: CreateExpenseUseCase,
    val update: UpdateExpenseUseCase,
    val delete: DeleteExpenseUseCase
)

data class UserUseCases(
    val getAll: GetAllUsersUseCase,
    val getByUsername: GetUserByUsernameUseCase,
    val create: CreateUserUseCase,
    val update: UpdateUserUseCase,
    val delete: DeleteUserUseCase,
    val countDirectors: CountDirectorsUseCase
)
