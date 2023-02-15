package com.abakan.electronics.data.startup

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.abakan.electronics.data.syncing.InjectedWorker

internal const val SYNC_WORK = "SyncWork"
class SyncInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_WORK,
                ExistingWorkPolicy.KEEP,
                InjectedWorker.buildWorkRequest()
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(WorkManagerInitializer::class.java)
}