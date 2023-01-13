package com.abakan.electronics.mycountries

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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

    @Test
    fun displayOneCountry_givenOneCountryIsLoaded() {
        composeTestRule.setContent {
            CountriesListScreen(
                state = CountriesListUIState.Success(
                    listOf(
                        Country(
                            Name("Spain"),
                            Capital("Madrid"),
                            CoatOfArmsImage("https://mainfacts.com/media/images/coats_of_arms/es.png")
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(
                R.string.country,
                "Spain"
            )
        ).assertIsDisplayed()
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.resources.getString(
                    R.string.capital,
                    "Madrid"
                )
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.coat_of_arms_description,
                    "Spain"
                )
            )
            .assertIsDisplayed()
    }
}