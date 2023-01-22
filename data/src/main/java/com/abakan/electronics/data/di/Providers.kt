package com.abakan.electronics.data.di

import android.content.Context
import androidx.room.Room
import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Providers {
    @Provides
    fun provideDao(countriesDatabase: CountriesDatabase): CountriesDao =
        countriesDatabase.countriesDao()

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext appContext: Context): CountriesDatabase =
        Room
            .databaseBuilder(appContext, CountriesDatabase::class.java, "Countries.db")
            .createFromAsset("Countries.db")
            .build()

    @Provides
    fun provideAndroidEngine(): HttpClientEngine = AndroidClientEngine(AndroidEngineConfig())
}