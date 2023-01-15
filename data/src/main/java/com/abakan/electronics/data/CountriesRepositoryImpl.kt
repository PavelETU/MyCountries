package com.abakan.electronics.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface CountriesRepository {
    fun getCountries(): Flow<List<Country>>
}

class CountriesRepositoryImpl @Inject constructor() : CountriesRepository {
    override fun getCountries(): Flow<List<Country>> =
        // return dummy data for now
        flow {
            emit(
                listOf(
                    Country(
                        "Spain",
                        "Madrid",
                        "https://mainfacts.com/media/images/coats_of_arms/es.png"
                    ),
                    Country(
                        "UK",
                        "London",
                        "https://mainfacts.com/media/images/coats_of_arms/gb.png"
                    ),
                    Country(
                        "US",
                        "Washington D.C.",
                        "https://mainfacts.com/media/images/coats_of_arms/us.png"
                    )
                )
            )
        }
}

data class Country(val name: String, val capital: String, val coatOfArmsUrl: String)