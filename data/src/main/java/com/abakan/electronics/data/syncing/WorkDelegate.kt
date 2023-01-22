package com.abakan.electronics.data.syncing

import androidx.work.ListenableWorker
import com.abakan.electronics.data.CountriesRepository

class WorkDelegate(private val repository: CountriesRepository) {
    suspend fun doWork(): ListenableWorker.Result =
        try {
            repository.sync()
            ListenableWorker.Result.success()
        } catch (t: Throwable) {
            repository.insertFallbackData()
            ListenableWorker.Result.retry()
        }
}
