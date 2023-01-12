package com.abakan.electronics.mycountries

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountriesListShould {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displayLoadingIndicator_givenStateIsLoading() {
        composeTestRule.setContent {
            CountriesListScreen(CountriesListUIState.Loading)
        }

        composeTestRule
            .onNodeWithTag("LoadingIndicator::ListOfCountries")
            .assertIsDisplayed()
    }
}