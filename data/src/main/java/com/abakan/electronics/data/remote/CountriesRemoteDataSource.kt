package com.abakan.electronics.data.remote

internal interface CountriesRemoteDataSource {
    suspend fun getCountries(): List<RemoteCountry>
}
