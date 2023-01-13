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
    private val listOfCountries = listOf(
        Country(
            Name("Spain"),
            Capital("Madrid"),
            CoatOfArmsImage("")
        ),
        Country(
            Name("Georgia"),
            Capital("Tbilisi"),
            CoatOfArmsImage("")
        ),
        Country(
            Name("Norway"),
            Capital("Oslo"),
            CoatOfArmsImage("")
        )
    )

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
        val name = "Spain"
        val capital = "Madrid"
        composeTestRule.setContent {
            CountriesListScreen(
                state = CountriesListUIState.Success(
                    listOf(
                        Country(
                            Name(name),
                            Capital(capital),
                            CoatOfArmsImage("")
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(
                R.string.country,
                name
            )
        ).assertIsDisplayed()
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.resources.getString(
                    R.string.capital,
                    capital
                )
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.coat_of_arms_description,
                    name
                )
            )
            .assertIsDisplayed()
    }

    @Test
    fun displayThreeCountries_givenThreeCountriesLoaded() {
        composeTestRule.setContent {
            CountriesListScreen(state = CountriesListUIState.Success(listOfCountries))
        }

        listOfCountries.forEach {
            composeTestRule.onNodeWithText(
                composeTestRule.activity.resources.getString(
                    R.string.country,
                    it.name.name
                )
            ).assertExists()
            composeTestRule.onNodeWithText(
                composeTestRule.activity.resources.getString(
                    R.string.capital,
                    it.capital.capital
                )
            ).assertExists()
            composeTestRule.onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(
                    R.string.coat_of_arms_description,
                    it.name.name
                )
            ).assertExists()
        }
    }
}