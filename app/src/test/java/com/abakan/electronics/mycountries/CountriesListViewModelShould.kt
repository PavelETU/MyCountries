package com.abakan.electronics.mycountries

import com.abakan.electronics.data.CountriesRepository
import com.abakan.electronics.data.syncing.SyncingMonitor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.abakan.electronics.data.Country as CountryFromDataModule

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesListViewModelShould {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var repository: CountriesRepository

    @RelaxedMockK
    private lateinit var syncingMonitor: SyncingMonitor

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `load data at first`() {
        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        assertEquals(CountriesListUIState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `map countries from repository to UI layer given syncing is done`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            ),
            CountryFromDataModule(
                "UK",
                "London",
                "https://mainfacts.com/media/images/coats_of_arms/gb.png"
            ),
            CountryFromDataModule(
                "US",
                "Washington D.C.",
                "https://mainfacts.com/media/images/coats_of_arms/us.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }
        every { syncingMonitor.isSyncing } returns flow { emit(false) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertTrue(viewModel.uiState.value is CountriesListUIState.Success)
        val result = (viewModel.uiState.value as CountriesListUIState.Success).countries
        assertEquals(countriesFromRepository.size, result.size)
        countriesFromRepository.forEachIndexed { index, country ->
            assertEquals(country.name, result[index].name.name)
            assertEquals(country.capital, result[index].capital.capital)
            assertEquals(country.coatOfArmsUrl, result[index].coatOfArmsImage.url)
        }
        collectJob.cancel()
    }

    @Test
    fun `be in loading state given syncing is in progress`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            ),
            CountryFromDataModule(
                "UK",
                "London",
                "https://mainfacts.com/media/images/coats_of_arms/gb.png"
            ),
            CountryFromDataModule(
                "US",
                "Washington D.C.",
                "https://mainfacts.com/media/images/coats_of_arms/us.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }
        every { syncingMonitor.isSyncing } returns flow { emit(true) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertTrue(viewModel.uiState.value is CountriesListUIState.Loading)
        collectJob.cancel()
    }

    @Test
    fun `map empty list to loading state`() = runTest {
        every { repository.getCountries() } returns flow { emit(emptyList()) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertTrue(viewModel.uiState.value is CountriesListUIState.Loading)
        collectJob.cancel()
    }

    @Test
    fun `display searchDialog on search button click`() = runTest {
        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.displaySearchDialog.collect() }

        viewModel.onSearchClick()

        assertTrue(viewModel.displaySearchDialog.value)
        collectJob.cancel()
    }

    @Test
    fun `hide searchDialog on cancel button click`() = runTest {
        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.displaySearchDialog.collect() }

        viewModel.onSearchClick()
        viewModel.onSearchCancel()

        assertFalse(viewModel.displaySearchDialog.value)
        collectJob.cancel()
    }

    @Test
    fun `perform search by name`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            ),
            CountryFromDataModule(
                "UK",
                "London",
                "https://mainfacts.com/media/images/coats_of_arms/gb.png"
            ),
            CountryFromDataModule(
                "US",
                "Washington D.C.",
                "https://mainfacts.com/media/images/coats_of_arms/us.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }
        every { syncingMonitor.isSyncing } returns flow { emit(false) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.onSearchPerform("sp", true)

        val result = (viewModel.uiState.value as CountriesListUIState.Success).countries
        assertEquals(1, result.size)
        assertEquals("Spain", result[0].name.name)
        assertEquals("Madrid", result[0].capital.capital)
        assertEquals("https://mainfacts.com/media/images/coats_of_arms/es.png", result[0].coatOfArmsImage.url)
        collectJob.cancel()
    }

    @Test
    fun `perform search by capital`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            ),
            CountryFromDataModule(
                "UK",
                "London",
                "https://mainfacts.com/media/images/coats_of_arms/gb.png"
            ),
            CountryFromDataModule(
                "US",
                "Washington D.C.",
                "https://mainfacts.com/media/images/coats_of_arms/us.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }
        every { syncingMonitor.isSyncing } returns flow { emit(false) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.onSearchPerform("lon", false)

        val result = (viewModel.uiState.value as CountriesListUIState.Success).countries
        assertEquals(1, result.size)
        assertEquals("UK", result[0].name.name)
        assertEquals("London", result[0].capital.capital)
        assertEquals("https://mainfacts.com/media/images/coats_of_arms/gb.png", result[0].coatOfArmsImage.url)
        collectJob.cancel()
    }

    @Test
    fun `close search dialog onSearchPerform`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.displaySearchDialog.collect() }
        viewModel.onSearchClick()
        viewModel.onSearchPerform("sp", true)

        assertFalse(viewModel.displaySearchDialog.value)
        collectJob.cancel()
    }

    @Test
    fun `display no results screen given search returned no results`() = runTest {
        val countriesFromRepository = listOf(
            CountryFromDataModule(
                "Spain",
                "Madrid",
                "https://mainfacts.com/media/images/coats_of_arms/es.png"
            ),
            CountryFromDataModule(
                "UK",
                "London",
                "https://mainfacts.com/media/images/coats_of_arms/gb.png"
            ),
            CountryFromDataModule(
                "US",
                "Washington D.C.",
                "https://mainfacts.com/media/images/coats_of_arms/us.png"
            )
        )
        every { repository.getCountries() } returns flow { emit(countriesFromRepository) }
        every { syncingMonitor.isSyncing } returns flow { emit(false) }

        val viewModel = CountriesListViewModel(repository, syncingMonitor)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.onSearchPerform("noResults", true)

        assertEquals(CountriesListUIState.NoSearchResults, viewModel.uiState.value)
        collectJob.cancel()
    }
}