package com.abakan.electronics.data.di

import com.abakan.electronics.data.CountriesRepository
import com.abakan.electronics.data.Country
import com.abakan.electronics.data.remote.CountriesRemoteDataSource
import com.abakan.electronics.data.remote.RemoteCountry
import com.abakan.electronics.data.syncing.SyncingMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestModule {
    @Binds
    fun bindRepository(countriesRepository: FakeRepository): CountriesRepository

    @Binds
    fun bindRemote(fakeClient: FakeRemoteDataSource): CountriesRemoteDataSource

    @Binds
    fun bindSyncingMonitor(syncMonitor: FakeSyncMonitor): SyncingMonitor
}

internal class FakeRepository @Inject constructor() : CountriesRepository {
    override fun getCountries(): Flow<List<Country>> {
        return flow { emit(listOf(Country("Mexico", "Mexico City", ""))) }
    }
    override suspend fun sync(): Boolean = true
}

internal class FakeRemoteDataSource @Inject constructor() : CountriesRemoteDataSource {
    override suspend fun getCountries(): List<RemoteCountry> = emptyList()
}

internal class FakeSyncMonitor @Inject constructor() : SyncingMonitor {
    override val isSyncing: Flow<Boolean>
        get() = flow { emit(true) }
}
