package com.abakan.electronics.mycountries

import com.abakan.electronics.data.CountriesRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CountriesListViewModelShould {
    @MockK
    private lateinit var repository: CountriesRepository

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `load data at first`() {
        val viewModel = CountriesListViewModel(repository)
        assertEquals(CountriesListUIState.Loading, viewModel.uiState.value)
    }
}