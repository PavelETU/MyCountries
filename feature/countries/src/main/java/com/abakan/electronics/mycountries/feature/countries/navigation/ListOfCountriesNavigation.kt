package com.abakan.electronics.mycountries.feature.countries.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abakan.electronics.mycountries.feature.countries.CountriesListRoute

const val COUNTRIES_ROUTE = "countries"

fun NavGraphBuilder.countriesScreen() {
    composable(COUNTRIES_ROUTE) { CountriesListRoute() }
}
