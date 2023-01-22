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
    private val repository: CountriesRepository
) : CoroutineWorker(appContext, workParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repository.sync()
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        fun buildWorkRequest() = OneTimeWorkRequestBuilder<DefaultWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).setInputData(InjectedWorker::class.workerData()).build()
    }
}