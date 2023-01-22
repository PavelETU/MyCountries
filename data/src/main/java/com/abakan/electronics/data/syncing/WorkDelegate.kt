package com.abakan.electronics.data.syncing

import androidx.work.ListenableWorker
import com.abakan.electronics.data.CountriesRepository

class WorkDelegate(private val repository: CountriesRepository) {
    suspend fun doWork(): ListenableWorker.Result =
        if (repository.sync())
            ListenableWorker.Result.success()
        else
            ListenableWorker.Result.retry()
}
