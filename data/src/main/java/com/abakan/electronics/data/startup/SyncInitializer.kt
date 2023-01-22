package com.abakan.electronics.data.startup

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.abakan.electronics.data.syncing.InjectedWorker

class SyncInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                "SyncWork",
                ExistingWorkPolicy.KEEP,
                InjectedWorker.buildWorkRequest()
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(WorkManagerInitializer::class.java)
}