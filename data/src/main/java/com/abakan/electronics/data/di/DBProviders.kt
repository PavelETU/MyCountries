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

@Module
@InstallIn(SingletonComponent::class)
internal class DBProviders {
    @Provides
    fun provideDao(countriesDatabase: CountriesDatabase): CountriesDao =
        countriesDatabase.countriesDao()

    @Provides
    fun provideDB(@ApplicationContext appContext: Context): CountriesDatabase =
        Room
            .databaseBuilder(appContext, CountriesDatabase::class.java, "Countries.db")
            .createFromAsset("Countries.db")
            .build()
}