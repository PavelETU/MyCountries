package com.abakan.electronics.mycountries

sealed interface CountriesListUIState {
    object Loading: CountriesListUIState
    data class Success(val countries: List<Country>): CountriesListUIState
    object NoSearchResults: CountriesListUIState
}
