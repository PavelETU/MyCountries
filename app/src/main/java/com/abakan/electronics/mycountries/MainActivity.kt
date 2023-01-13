package com.abakan.electronics.mycountries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.abakan.electronics.mycountries.ui.theme.MyCountriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCountriesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountriesListScreen(CountriesListUIState.Loading)
                }
            }
        }
    }
}

@Composable
fun CountriesListScreen(state: CountriesListUIState) {
    when (state) {
        is CountriesListUIState.Loading -> LoadingState()
        is CountriesListUIState.Success -> ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            val country = state.countries[0]
            AsyncImage(
                model = country.coatOfArmsImage.url,
                contentDescription = stringResource(
                    id = R.string.coat_of_arms_description,
                    country.name.name
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.country, country.name.name),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.capital, country.capital.capital),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("LoadingIndicator::ListOfCountries")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    MyCountriesTheme {
        CountriesListScreen(CountriesListUIState.Loading)
    }
}

@Preview(showBackground = true)
@Composable
fun OneCountryPreview() {
    MyCountriesTheme {
        CountriesListScreen(
            CountriesListUIState.Success(
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
}