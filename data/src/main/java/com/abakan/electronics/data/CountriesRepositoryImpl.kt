package com.abakan.electronics.data

import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountryEntity
import com.abakan.electronics.data.db.asExternal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CountriesRepositoryImpl @Inject constructor(private val countriesDao: CountriesDao) :
    CountriesRepository {
    override fun getCountries(): Flow<List<Country>> =
        countriesDao
            .getCountries()
            .map { it.map(CountryEntity::asExternal) }
}
