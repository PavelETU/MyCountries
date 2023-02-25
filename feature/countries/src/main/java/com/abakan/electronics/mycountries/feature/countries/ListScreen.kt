package com.abakan.electronics.mycountries.feature.countries

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.abakan.electronics.mycountries.feature.list.R
import com.abakan.electronics.mycountries.ui.MyCountriesTheme
import kotlinx.coroutines.flow.drop

@Composable
internal fun CountriesListRoute(viewModel: CountriesListViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val displaySearch by viewModel.displaySearchDialog.collectAsStateWithLifecycle()
    CountriesListScreen(
        state,
        displaySearch,
        onSearchClick = { viewModel.onSearchClick() },
        onSearchAction = { term, type -> viewModel.onSearchPerform(term, type) },
        onSearchClose = { viewModel.onSearchCancel() })
}

@Composable
fun CountriesListScreen(
    state: CountriesListUIState,
    displaySearchDialog: Boolean,
    onSearchClick: () -> Unit,
    onSearchAction: (String, Boolean) -> Unit,
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
    onSearchAction: (String, Boolean) -> Unit,
    onSearchClose: () -> Unit
) {
    val verticalScroll = rememberLazyGridState()
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp), contentPadding = it, state = verticalScroll
        ) {
            items(items = state.countries) { country ->
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
        ColorPainter(Color.Cyan)
    else
        null

@OptIn(ExperimentalTextApi::class)
@Composable
private fun NoResultsState(
    displaySearchDialog: Boolean,
    onSearchClick: () -> Unit,
    onSearchAction: (String, Boolean) -> Unit,
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
private fun SearchDialog(
    onSearchAction: (String, Boolean) -> Unit,
    onSearchClose: () -> Unit
) {
    var searchTerm by rememberSaveable { mutableStateOf("") }
    var searchByName by rememberSaveable { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    AlertDialog(onDismissRequest = onSearchClose, title = {
        Text(text = stringResource(id = R.string.search_country))
    }, text = {
        Column {
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
                    onSearchAction(searchTerm, searchByName)
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
            val primaryColor = MaterialTheme.colorScheme.primary
            val rememberTextMeasurer = rememberTextMeasurer()
            val measuredText =
                rememberTextMeasurer.measure(AnnotatedString(stringResource(id = R.string.search_by)))
            val middleOfText = measuredText.size.height / 2f
            Column(
                Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp, end = 5.dp)
                    .selectableGroup()
                    .drawBehind {
                        val twentiethOfWidth = size.width / 20f
                        drawText(measuredText, topLeft = Offset(twentiethOfWidth, 0F))
                        drawLine(
                            color = primaryColor,
                            Offset(0f, middleOfText),
                            Offset(0f, size.height)
                        )
                        drawLine(
                            color = primaryColor,
                            Offset(0f, size.height),
                            Offset(size.width, size.height)
                        )
                        drawLine(
                            color = primaryColor,
                            Offset(size.width, size.height),
                            Offset(size.width, middleOfText)
                        )
                        drawLine(
                            color = primaryColor,
                            Offset(size.width, middleOfText),
                            Offset(twentiethOfWidth + measuredText.size.width + 8f, middleOfText)
                        )
                        drawLine(
                            color = primaryColor,
                            Offset(0f, middleOfText),
                            Offset(twentiethOfWidth - 8f, middleOfText)
                        )
                    }
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Row(
                        Modifier
                            .height(60.dp)
                            .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                            .selectable(
                                selected = searchByName,
                                onClick = { searchByName = true },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = searchByName, onClick = null)
                        Text(
                            text = stringResource(id = R.string.by_name),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Row(
                        Modifier
                            .height(60.dp)
                            .padding(top = 5.dp, end = 10.dp)
                            .selectable(
                                selected = searchByName,
                                onClick = { searchByName = false },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = !searchByName, onClick = null)
                        Text(
                            text = stringResource(id = R.string.by_capital),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    }, confirmButton = {
        Button(
            modifier = Modifier.testTag("inDialog"),
            onClick = { onSearchAction(searchTerm, searchByName) }) {
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
        CountriesListScreen(CountriesListUIState.Loading, false, {}, { _, _ -> }, {})
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
            { _, _ -> }, {}
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
            { _, _ -> }, {}
        )
    }
}

@Preview(showBackground = true, heightDp = 300)
@Composable
fun NoSearchResultsPreview() {
    MyCountriesTheme {
        CountriesListScreen(CountriesListUIState.NoSearchResults, false, {}, { _, _ -> }, {})
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
fun SearchDialogPreview() {
    MyCountriesTheme {
        SearchDialog({ _, _ -> }, {})
    }
}
