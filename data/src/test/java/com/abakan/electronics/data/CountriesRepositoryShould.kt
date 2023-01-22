package com.abakan.electronics.data

import com.abakan.electronics.data.db.CountriesDao
import com.abakan.electronics.data.db.CountryEntity
import com.abakan.electronics.data.remote.*
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesRepositoryShould {
    @RelaxedMockK
    private lateinit var countriesDao: CountriesDao

    @RelaxedMockK
    private lateinit var countriesRemoteDataSource: CountriesRemoteDataSource

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

    @Test
    fun `sync database with the network`() = runTest {
        val countries = listOf(
            RemoteCountry(
                Name("Spain"),
                listOf("Madrid"),
                CoatOfArmsResponse(png = "https://mainfacts.com/media/images/coats_of_arms/es.png")
            ),
            RemoteCountry(
                Name("UK"),
                listOf("London"),
                CoatOfArmsResponse("https://mainfacts.com/media/images/coats_of_arms/gb.png")
            ),
            RemoteCountry(
                Name("US"),
                listOf("Washington D.C."),
                CoatOfArmsResponse("https://mainfacts.com/media/images/coats_of_arms/us.png")
            )
        )
        coEvery { countriesRemoteDataSource.getCountries() } returns countries
        repository.sync()
        coVerify(exactly = 1) { countriesRemoteDataSource.getCountries() }
        coVerify(exactly = 1) {
            countriesDao.insertAll(
                listOf(
                    CountryEntity(
                        0,
                        "Spain",
                        "Madrid",
                        "https://mainfacts.com/media/images/coats_of_arms/es.png"
                    ),
                    CountryEntity(
                        1,
                        "UK",
                        "London",
                        "https://mainfacts.com/media/images/coats_of_arms/gb.png"
                    ),
                    CountryEntity(
                        2,
                        "US",
                        "Washington D.C.",
                        "https://mainfacts.com/media/images/coats_of_arms/us.png"
                    )
                )
            )
        }
    }

    @Test
    fun `insert 3 countries into db on insertFallbackData`() = runTest {
        repository.insertFallbackData()
        coVerify(exactly = 1) {
            countriesDao.insertAll(FALLBACK_DATA)
        }
    }
}
