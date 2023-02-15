package com.abakan.electronics.data.syncing

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.abakan.electronics.data.startup.SYNC_WORK
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

class WorkManagerSyncingMonitor @Inject constructor(@ApplicationContext context: Context) :
    SyncingMonitor {
    override val isSyncing: Flow<Boolean> =
        Transformations.map(
            WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SYNC_WORK),
            MutableList<WorkInfo>::anyRunning
        )
            .asFlow()
            .conflate()


}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }