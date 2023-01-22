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
import org.junit.Ignore
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
    fun `return success given sync returned true`() = runTest {
        coEvery { repository.sync() } returns true
        val result = worker.doWork()

        coVerify(exactly = 1) { repository.sync() }
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `return retry given sync returned false`() = runTest {
        coEvery { repository.sync() } returns false

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }
}