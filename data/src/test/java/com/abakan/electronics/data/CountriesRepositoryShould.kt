package com.abakan.electronics.data

import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountryEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesRepositoryShould {
    @RelaxedMockK
    private lateinit var countriesDao: CountriesDao
    @InjectMockKs
    private lateinit var repository: CountriesRepositoryImpl

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `get countries from db and map them to external model`() = runTest {
        every { countriesDao.getCountries() } returns flow {
            emit(listOf(CountryEntity(1, "Spain", "Madrid", "www.coatOfArms.com")))
        }

        val countries = repository.getCountries().last()

        verify(exactly = 1) { countriesDao.getCountries() }
        assertEquals(1, countries.size)
        assertEquals("Spain", countries[0].name)
        assertEquals("Madrid", countries[0].capital)
        assertEquals("www.coatOfArms.com", countries[0].coatOfArmsUrl)
    }
}
