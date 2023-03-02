package com.abakan.electronics.mycountries.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.abakan.electronics.mycountries.feature.details.CountryDetailsRoute

private const val COUNTRY_NAME = "countryName"

fun NavController.navigateToDetails(countryName: String) {
    navigate("country_route/$countryName")
}

fun NavGraphBuilder.detailsScreen() {
    composable(route = "country_route/{$COUNTRY_NAME}",
        arguments = listOf(navArgument(COUNTRY_NAME) { type = NavType.StringType })
    ) {
        CountryDetailsRoute(countryName = it.arguments?.getString(COUNTRY_NAME) ?: "")
    }
}