package com.abakan.electronics.mycountries.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abakan.electronics.mycountries.ui.MyCountriesTheme

@Composable
internal fun CountryDetailsRoute(countryName: String) {
    CountryScreen(countryName = countryName)
}

@Composable
fun CountryScreen(countryName: String) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "All about $countryName", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun CountryPreview() {
    MyCountriesTheme {
        CountryScreen(countryName = "Mexico")
    }
}
