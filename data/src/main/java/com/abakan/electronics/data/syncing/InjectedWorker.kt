package com.abakan.electronics.data.syncing

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.abakan.electronics.data.CountriesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class InjectedWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workParams: WorkerParameters,
    repository: CountriesRepository
) : CoroutineWorker(appContext, workParams) {

    private val workDelegate = WorkDelegate(repository)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        workDelegate.doWork()
    }

    companion object {
        fun buildWorkRequest() = OneTimeWorkRequestBuilder<DefaultWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(InjectedWorker::class.workerData()).build()
    }
}