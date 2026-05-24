package com.wholesale.manager.domain.usecase.purchase

import com.wholesale.manager.domain.model.PurchasedRaw
import com.wholesale.manager.domain.repository.PurchasedRawRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class PurchaseUseCasesTest {
    private val repository = mockk<PurchasedRawRepository>()

    @Test
    fun createPurchase_delegatesToRepository() = runTest {
        val useCase = CreatePurchaseUseCase(repository)
        val purchase = samplePurchase()

        coEvery { repository.create(purchase) } returns Unit

        useCase(purchase)

        coVerify(exactly = 1) { repository.create(purchase) }
    }



    @Test
    fun getPurchaseById_delegatesToRepository() = runTest {
        val useCase = GetPurchaseByIdUseCase(repository)
        val purchase = samplePurchase()
        coEvery { repository.getById(purchase.id) } returns purchase

        assertEquals(purchase, useCase(purchase.id))
    }

    @Test
    fun updatePurchase_delegatesToRepository() = runTest {
        val useCase = UpdatePurchaseUseCase(repository)
        val purchase = samplePurchase()
        coEvery { repository.update(purchase) } returns Unit

        useCase(purchase)

        coVerify(exactly = 1) { repository.update(purchase) }
    }

    @Test
    fun deletePurchase_delegatesToRepository() = runTest {
        val useCase = DeletePurchaseUseCase(repository)
        coEvery { repository.delete("p1") } returns Unit

        useCase("p1")

        coVerify(exactly = 1) { repository.delete("p1") }
    }

    private fun samplePurchase() = PurchasedRaw(
        id = UUID.randomUUID().toString(),
        number = 1,
        lastModified = "2026-05-21T10:00:00Z",
        type = "Кедровая шишка",
        quantityKg = 120.0,
        purchasePriceTotal = 36000.0,
        pricePerKg = 300.0,
        supplierName = "ООО Лес",
        purchaseDate = "2026-05-21",
        status = PurchasedRaw.STATUS_PENDING,
        batchId = null
    )
}
