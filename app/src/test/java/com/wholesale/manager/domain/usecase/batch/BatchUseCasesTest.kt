package com.wholesale.manager.domain.usecase.batch

import com.wholesale.manager.domain.model.Batch
import com.wholesale.manager.domain.repository.BatchRepository
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

class BatchUseCasesTest {
    private val repository = mockk<BatchRepository>()

    @Test
    fun createBatch_delegatesToRepository() = runTest {
        val useCase = CreateBatchUseCase(repository)
        val batch = sampleBatch()
        coEvery { repository.create(batch) } returns Unit

        useCase(batch)

        coVerify(exactly = 1) { repository.create(batch) }
    }

    @Test
    fun getAllBatches_returnsRepositoryFlow() {
        val useCase = GetAllBatchesUseCase(repository)
        val items = listOf(sampleBatch())
        every { repository.getAll() } returns flowOf(items)

        val result = useCase()

        assertEquals(items, result.first())
    }

    @Test
    fun updateBatch_delegatesToRepository() = runTest {
        val useCase = UpdateBatchUseCase(repository)
        val batch = sampleBatch()
        coEvery { repository.update(batch) } returns Unit

        useCase(batch)

        coVerify(exactly = 1) { repository.update(batch) }
    }

    @Test
    fun deleteBatch_delegatesToRepository() = runTest {
        val useCase = DeleteBatchUseCase(repository)
        coEvery { repository.delete("b1") } returns Unit

        useCase("b1")

        coVerify(exactly = 1) { repository.delete("b1") }
    }

    @Test
    fun getBatchById_delegatesToRepository() = runTest {
        val useCase = GetBatchByIdUseCase(repository)
        val batch = sampleBatch()
        coEvery { repository.getById(batch.id) } returns batch

        assertEquals(batch, useCase(batch.id))
    }

    private fun sampleBatch() = Batch(
        id = UUID.randomUUID().toString(),
        number = "001",
        formationDate = "2026-05-21",
        purchaseId = UUID.randomUUID().toString(),
        rawQuantityKg = 120.0,
        outputKg = 48.0,
        outputPercent = 40,
        costPrice = 36000.0,
        optimalPricePerKg = 750.0,
        status = Batch.STATUS_ACTIVE,
        lastModified = "2026-05-21T11:00:00Z"
    )
}
