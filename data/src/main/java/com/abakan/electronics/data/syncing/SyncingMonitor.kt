package com.abakan.electronics.data.syncing

import kotlinx.coroutines.flow.Flow

interface SyncingMonitor {
    val isSyncing: Flow<Boolean>
}
