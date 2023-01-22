package com.abakan.electronics.mycountries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abakan.electronics.data.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.abakan.electronics.data.Country as DataCountry

@HiltViewModel
class CountriesListViewModel @Inject constructor(repository: CountriesRepository) : ViewModel() {
    val uiState: StateFlow<CountriesListUIState> =
        repository
            .getCountries()
            .map { it.toUIState() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                CountriesListUIState.Loading
            )

    private fun List<DataCountry>.toUIState(): CountriesListUIState =
        if (isNotEmpty()) {
            CountriesListUIState.Success(map {
                Country(Name(it.name), Capital(it.capital), CoatOfArmsImage(it.coatOfArmsUrl))
            })
        } else {
            CountriesListUIState.Loading
        }
}
