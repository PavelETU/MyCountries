package com.abakan.electronics.mycountries

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.abakan.electronics.mycountries.ui.theme.MyCountriesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.drop

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MyCountriesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CountriesListViewModel = viewModel()
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    val displaySearch by viewModel.displaySearchDialog.collectAsStateWithLifecycle()
                    CountriesListScreen(
                        state,
                        displaySearch,
                        onSearchClick = { viewModel.onSearchClick() },
                        onSearchAction = { viewModel.onSearchPerform(it) },
                        onSearchClose = { viewModel.onSearchCancel() })
                }
            }
        }
    }
}

@Composable
fun CountriesListScreen(
    state: CountriesListUIState,
    displaySearchDialog: Boolean,
    onSearchClick: () -> Unit,
    onSearchAction: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    when (state) {
        is CountriesListUIState.Loading -> LoadingState()
        is CountriesListUIState.Success -> CountryList(
            state,
            displaySearchDialog,
            onSearchClick,
            onSearchAction,
            onSearchClose
        )
        is CountriesListUIState.NoSearchResults -> NoResultsState(
            displaySearchDialog,
            onSearchClick,
            onSearchAction,
            onSearchClose
        )
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun CountryList(
    state: CountriesListUIState.Success,
    displaySearchDialog: Boolean,
    onSearchClick: () -> Unit,
    onSearchAction: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    val verticalScroll = rememberLazyListState()
    var fabExtended by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(verticalScroll) {
        var prevScroll = 0
        var prevFirstVisibleItem = 0
        snapshotFlow {
            verticalScroll.firstVisibleItemScrollOffset
        }.drop(1).collect {
            if (prevFirstVisibleItem == verticalScroll.firstVisibleItemIndex) fabExtended =
                it < prevScroll
            prevScroll = it
            prevFirstVisibleItem = verticalScroll.firstVisibleItemIndex
        }
    }
    Scaffold(
        Modifier.fillMaxSize(),
        floatingActionButton = { SearchButton(onSearchClick, fabExtended) }) {
        if (displaySearchDialog) {
            SearchDialog(onSearchAction, onSearchClose)
        }
        LazyColumn(
            contentPadding = it, state = verticalScroll
        ) {
            items(state.countries) { country ->
                country.ToComposable()
            }
        }
    }
}

@Composable
private fun SearchButton(onSearchClick: () -> Unit, fabExtended: Boolean) {
    FloatingActionButton(
        modifier = Modifier.padding(0.dp),
        onClick = onSearchClick
    ) {
        Row(
            Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search)
            )
            AnimatedVisibility(visible = fabExtended) {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(id = R.string.search)
                )
            }
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun NoResultsState(
    displaySearchDialog: Boolean,
    onSearchClick: () -> Unit,
    onSearchAction: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(id = R.string.no_results),
            style = TextStyle(
                brush = Brush.linearGradient(
                    listOf(Color.Blue, Color.Magenta)
                )
            ),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )
        OutlinedButton(
            onClick = onSearchClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.change_term))
        }
        if (displaySearchDialog) {
            SearchDialog(onSearchAction, onSearchClose)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchDialog(
    onSearchAction: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    var searchTerm by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    AlertDialog(onDismissRequest = onSearchClose, title = {
        Text(text = stringResource(id = R.string.search_country))
    }, text = {
        OutlinedTextField(
            value = searchTerm, onValueChange = { searchTerm = it },
            modifier = Modifier.padding(5.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onSearchAction(searchTerm)
            }),
            label = {
                Text(
                    text = stringResource(id = R.string.search_term),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
    }, confirmButton = {
        Button(modifier = Modifier.testTag("inDialog"), onClick = { onSearchAction(searchTerm) }) {
            Text(text = stringResource(id = R.string.search))
        }
    }, dismissButton = {
        Button(onClick = onSearchClose) {
            Text(text = stringResource(id = R.string.cancel))
        }
    })
}

@Preview(showBackground = true, heightDp = 100)
@Composable
fun LoadingPreview() {
    MyCountriesTheme {
        CountriesListScreen(CountriesListUIState.Loading, false, {}, {}, {})
    }
}

@Preview(heightDp = 400, showBackground = true)
@Composable
fun OneCountryPreview() {
    MyCountriesTheme {
        val context = LocalContext.current
        CountriesListScreen(
            CountriesListUIState.Success(
                listOf(
                    Country(
                        Name("Spain"),
                        Capital("Madrid"),
                        CoatOfArmsImage("https://mainfacts.com/media/images/coats_of_arms/es.png")
                    )
                )
            ),
            false,
            {
                Toast.makeText(context, "Search button clicked", Toast.LENGTH_SHORT).show()
            },
            {}, {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ThreeCountriesPreview() {
    MyCountriesTheme {
        val context = LocalContext.current
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
            ),
            false,
            {
                Toast.makeText(context, "Search button clicked", Toast.LENGTH_SHORT).show()
            },
            {}, {}
        )
    }
}

@Preview(showBackground = true, heightDp = 300)
@Composable
fun NoSearchResultsPreview() {
    MyCountriesTheme {
        CountriesListScreen(CountriesListUIState.NoSearchResults, false, {}, {}, {})
    }
}
