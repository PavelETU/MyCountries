package com.abakan.electronics.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountriesDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
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
}