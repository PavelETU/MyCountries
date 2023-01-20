package com.abakan.electronics.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getCountries(): Flow<List<CountryEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg countries: CountryEntity)
}
