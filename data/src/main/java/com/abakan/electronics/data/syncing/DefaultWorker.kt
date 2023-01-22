package com.abakan.electronics.data.syncing

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerEntryPoint {
    fun hiltWorkerFactory(): HiltWorkerFactory
}

private const val WORKER_NAME = "DefaultWorker"

internal fun KClass<out CoroutineWorker>.workerData() =
    Data.Builder()
        .putString(WORKER_NAME, qualifiedName)
        .build()

class DefaultWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val className = workerParams.inputData.getString(WORKER_NAME) ?: ""

    private val injectedWorker =
        EntryPointAccessors.fromApplication<HiltWorkerEntryPoint>(appContext)
            .hiltWorkerFactory()
            .createWorker(appContext, className, workerParams) as CoroutineWorker

    override suspend fun doWork(): Result = injectedWorker.doWork()
}