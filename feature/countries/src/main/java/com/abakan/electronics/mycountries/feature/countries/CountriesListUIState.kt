package com.abakan.electronics.mycountries.feature.countries

sealed interface CountriesListUIState {
    object Loading: CountriesListUIState
    data class Success(val countries: List<Country>): CountriesListUIState
    object NoSearchResults: CountriesListUIState
}
