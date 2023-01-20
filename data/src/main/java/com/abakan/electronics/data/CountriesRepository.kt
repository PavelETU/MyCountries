package com.abakan.electronics.data

import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    fun getCountries(): Flow<List<Country>>
    suspend fun sync()
}