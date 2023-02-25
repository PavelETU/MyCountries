package com.abakan.electronics.mycountries

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.abakan.electronics.mycountries.feature.countries.navigation.COUNTRIES_ROUTE
import com.abakan.electronics.mycountries.feature.countries.navigation.countriesScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = COUNTRIES_ROUTE) {
        countriesScreen()
    }
}