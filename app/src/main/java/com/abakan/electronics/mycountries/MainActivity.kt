package com.abakan.electronics.mycountries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
        is CountriesListUIState.Success -> CountryList(state)
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

@Composable
private fun CountryList(state: CountriesListUIState.Success) {
    LazyColumn {
        items(state.countries) { country ->
            country.ToComposable()
        }
    }
}

@Composable
private fun Country.ToComposable() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        AsyncImage(
            model = coatOfArmsImage.url,
            contentDescription = stringResource(
                id = R.string.coat_of_arms_description,
                name.name
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 5.dp),
            placeholder = returnDummyPainterForPreviewOrNull()
        )
        Text(
            text = stringResource(id = R.string.country, name.name),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.capital, capital.capital),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun returnDummyPainterForPreviewOrNull() =
    if (LocalInspectionMode.current)
        painterResource(id = R.drawable.ic_launcher_foreground)
    else
        null

@Preview(showBackground = true, heightDp = 100)
@Composable
fun LoadingPreview() {
    MyCountriesTheme {
        CountriesListScreen(CountriesListUIState.Loading)
    }
}

@Preview(heightDp = 400, showBackground = true)
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

@Preview(showBackground = true)
@Composable
fun ThreeCountriesPreview() {
    MyCountriesTheme {
        CountriesListScreen(
            CountriesListUIState.Success(
                listOf(
                    Country(
                        Name("Spain"),
                        Capital("Madrid"),
                        CoatOfArmsImage("https://mainfacts.com/media/images/coats_of_arms/es.png")
                    ),
                    Country(
                        Name("Georgia"),
                        Capital("Tbilisi"),
                        CoatOfArmsImage("https://mainfacts.com/media/images/coats_of_arms/ge.png")
                    ),
                    Country(
                        Name("Norway"),
                        Capital("Oslo"),
                        CoatOfArmsImage("https://mainfacts.com/media/images/coats_of_arms/no.png")
                    )
                )
            )
        )
    }
}
