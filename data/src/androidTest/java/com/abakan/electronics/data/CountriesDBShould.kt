package com.abakan.electronics.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountriesDatabase
import com.abakan.electronics.data.db.CountryEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesDBShould {
    private lateinit var countriesDao: CountriesDao
    private lateinit var db: CountriesDatabase

    @Before
    fun setUp() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, CountriesDatabase::class.java)
            .build()
        countriesDao = db.countriesDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun returnEmptyListForEmptyDB() = runTest {
        val countries = countriesDao.getCountries().first()
        assertTrue(countries.isEmpty())
    }

    @Test
    fun replaceOldCountriesWhileUpdate() = runTest {
        countriesDao.insertAll(
            CountryEntity(0, "OldCountry", "OldCapital", "OldCoatOfArms"),
            CountryEntity(1, "OldCountry2", "OldCapital2", "OldCoatOfArms2"),
            CountryEntity(2, "OldCountry3", "OldCapital3", "OldCoatOfArms3")
        )
        countriesDao.insertAll(
            CountryEntity(0, "NewCountry", "NewCapital", "NewCoatOfArms"),
            CountryEntity(1, "NewCountry2", "NewCapital2", "NewCoatOfArms2"),
            CountryEntity(2, "NewCountry3", "NewCapital3", "NewCoatOfArms3")
        )
        val countries = countriesDao.getCountries().first()

        assertEquals(3, countries.size)
        assertEquals(0, countries[0].uid)
        assertEquals("NewCountry", countries[0].name)
        assertEquals("NewCapital", countries[0].capital)
        assertEquals("NewCoatOfArms", countries[0].coatOfArmsUrl)
        assertEquals("NewCountry2", countries[1].name)
        assertEquals("NewCapital2", countries[1].capital)
        assertEquals("NewCoatOfArms2", countries[1].coatOfArmsUrl)
        assertEquals("NewCountry3", countries[2].name)
        assertEquals("NewCapital3", countries[2].capital)
        assertEquals("NewCoatOfArms3", countries[2].coatOfArmsUrl)
    }
}