package com.abakan.electronics.data.syncing

import androidx.work.ListenableWorker
import com.abakan.electronics.data.CountriesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WorkDelegateShould {
    @RelaxedMockK
    private lateinit var repository: CountriesRepository
    @InjectMockKs
    private lateinit var worker: WorkDelegate

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `return success given sync completed`() = runTest {
        val result = worker.doWork()

        coVerify(exactly = 1) { repository.sync() }
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `return retry given sync threw an exception`() = runTest {
        coEvery { repository.sync() } throws RuntimeException()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }

    @Test
    fun `insert fallback data given sync failed`() = runTest {
        coEvery { repository.sync() } throws RuntimeException()

        worker.doWork()

        coVerify(exactly = 1) { repository.insertFallbackData() }
    }
}