package com.abakan.electronics.data

import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountryEntity
import com.abakan.electronics.data.db.asExternal
import com.abakan.electronics.data.remote.CountriesRemoteDataSource
import com.abakan.electronics.data.remote.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CountriesRepositoryImpl @Inject constructor(
    private val countriesDao: CountriesDao,
    private val remoteSource: CountriesRemoteDataSource
) : CountriesRepository {
    override fun getCountries(): Flow<List<Country>> =
        countriesDao
            .getCountries()
            .map { it.map(CountryEntity::asExternal) }

    override suspend fun sync(): Boolean {
        var networkCallIsSuccessful = true
        val countriesToInsert = try {
            remoteSource.getCountries().mapIndexed { index, remoteCountry ->
                remoteCountry.toEntity(
                    index
                )
            }
        } catch (t: Throwable) {
            networkCallIsSuccessful = false
            FALLBACK_DATA
        }
        if (networkCallIsSuccessful || countriesDao.getCount() == 0L) {
            countriesDao.insertAll(countriesToInsert)
        }
        return networkCallIsSuccessful
    }
}
