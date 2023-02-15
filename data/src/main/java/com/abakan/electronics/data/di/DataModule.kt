package com.abakan.electronics.data.di

import com.abakan.electronics.data.CountriesRepository
import com.abakan.electronics.data.CountriesRepositoryImpl
import com.abakan.electronics.data.remote.CountriesRemoteDataSource
import com.abakan.electronics.data.remote.KtorClient
import com.abakan.electronics.data.syncing.SyncingMonitor
import com.abakan.electronics.data.syncing.WorkManagerSyncingMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.android.*

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindRepository(countriesRepository: CountriesRepositoryImpl): CountriesRepository

    @Binds
    fun bindRemote(ktorClient: KtorClient): CountriesRemoteDataSource

    @Binds
    fun bindSyncingMonitor(syncMonitor: WorkManagerSyncingMonitor): SyncingMonitor
}