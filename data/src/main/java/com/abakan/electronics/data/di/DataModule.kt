package com.abakan.electronics.data.di

import com.abakan.electronics.data.CountriesRepository
import com.abakan.electronics.data.CountriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindRepository(countriesRepository: CountriesRepositoryImpl): CountriesRepository
}