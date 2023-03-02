package com.abakan.electronics.mycountries

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MyCountriesNavigationShould {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateToCountriesAtTheStart() {
        composeTestRule
            .onNodeWithText("Country: Mexico")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Capital: Mexico City")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToDetailsScreenOnItemClick() {
        composeTestRule
            .onNodeWithText("Country: Mexico")
            .performClick()

        composeTestRule
            .onNodeWithText("All about Mexico")
            .assertIsDisplayed()
    }
}
