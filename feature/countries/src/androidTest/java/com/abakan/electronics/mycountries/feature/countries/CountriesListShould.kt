package com.abakan.electronics.mycountries.feature.countries

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abakan.electronics.mycountries.feature.list.R
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
            CountriesListScreen({}, CountriesListUIState.Loading, false, {}, { _, _ -> }, {})
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
                {},
                state = CountriesListUIState.Success(
                    listOf(
                        Country(
                            Name(name),
                            Capital(capital),
                            CoatOfArmsImage("")
                        )
                    )
                ),
                false,
                {}, { _, _ -> }, {},
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
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries),
                false,
                {},
                { _, _ -> },
                {})
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

    @Test
    fun displayClickableSearchButton_givenCountriesAreLoaded() {
        var searchButtonClicked = false
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries), false,
                {
                    searchButtonClicked = true
                },
                { _, _ -> }, {}
            )
        }

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.resources.getString(R.string.search)
        )
            .assertIsDisplayed()
            .performClick()
        assertTrue(searchButtonClicked)
    }

    @Test
    fun displaySearchDialog_givenDisplayDialogParameterIsTrue() {
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries),
                true,
                {},
                { _, _ -> }, {}
            )
        }

        assertSearchDialogIsDisplayed()
    }

    @Test
    fun performSearchByNameWithTheCorrectTerm_givenSearchInSearchDialogClicked() {
        var searchPerformed = false
        var searchByName = false
        var searchTerm = ""
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries),
                true,
                onSearchClick = {},
                { searchQuery, searchBy ->
                    searchPerformed = true
                    searchTerm = searchQuery
                    searchByName = searchBy
                }, {}
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.search_term))
            .performTextInput("United")
        composeTestRule
            .onNode(
                hasText(composeTestRule.activity.resources.getString(R.string.search)) and hasTestTag(
                    "inDialog"
                )
            )
            .performClick()

        assertTrue(searchPerformed)
        assertTrue(searchByName)
        assertEquals("United", searchTerm)
    }

    @Test
    fun performSearchByCapital_givenSearchByCapitalClicked() {
        var searchPerformed = false
        var searchByName = true
        var searchTerm = ""
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries),
                true,
                onSearchClick = {},
                { searchQuery, searchBy ->
                    searchPerformed = true
                    searchTerm = searchQuery
                    searchByName = searchBy
                }, {}
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.search_term))
            .performTextInput("Lon")
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.by_capital))
            .performClick()
        composeTestRule
            .onNode(
                hasText(composeTestRule.activity.resources.getString(R.string.search)) and hasTestTag(
                    "inDialog"
                )
            )
            .performClick()

        assertTrue(searchPerformed)
        assertFalse(searchByName)
        assertEquals("Lon", searchTerm)
    }

    @Test
    fun closeSearchDialog_givenCancelClicked() {
        var closeEventTriggered = false
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.Success(listOfCountries),
                true,
                onSearchClick = {}, onSearchAction = { _, _ -> }) {
                closeEventTriggered = true
            }
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.cancel))
            .performClick()

        assertTrue(closeEventTriggered)
    }

    @Test
    fun displayNoResults_givenNoSearchResultsState() {
        composeTestRule.setContent {
            CountriesListScreen(
                {},
                CountriesListUIState.NoSearchResults,
                false,
                {},
                { _, _ -> },
                {})
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.no_results))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.change_term))
            .assertIsDisplayed()
    }

    @Test
    fun triggerOnSearchClickFromNoResultsScreen_givenChangeTermClicked() {
        var onSearchClick = false
        composeTestRule.setContent {
            CountriesListScreen({},
                CountriesListUIState.NoSearchResults,
                false,
                { onSearchClick = true },
                { _, _ -> },
                {})
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.change_term))
            .performClick()

        assertTrue(onSearchClick)
    }

    @Test
    fun displaySearchDialogFromNoResultsScreen_givenDisplayDialogParameterIsTrue() {
        composeTestRule.setContent {
            CountriesListScreen({},
                state = CountriesListUIState.NoSearchResults,
                true,
                {}, { _, _ -> }, {}
            )
        }

        assertSearchDialogIsDisplayed()
    }

    @Test
    fun triggerNavigationIntoDetails_givenCountryClicked() {
        var countryToNavigateInto = ""
        composeTestRule.setContent {
            CountriesListScreen({ countryToNavigateInto = it },
                state = CountriesListUIState.Success(listOfCountries),
                false,
                {}, { _, _ -> }, {}
            )
        }

        composeTestRule
            .onNodeWithText("Capital: ${listOfCountries[1].capital.capital}")
            .performClick()

        assertEquals(listOfCountries[1].name.name, countryToNavigateInto)
    }

    private fun assertSearchDialogIsDisplayed() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.search_country))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.search_term))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.by_name))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.by_capital))
            .assertIsDisplayed()
        composeTestRule
            .onNode(
                hasText(composeTestRule.activity.resources.getString(R.string.search)) and hasTestTag(
                    "inDialog"
                )
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.cancel))
            .assertIsDisplayed()
    }
}
