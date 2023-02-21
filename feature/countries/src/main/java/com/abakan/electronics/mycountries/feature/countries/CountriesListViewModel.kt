package com.abakan.electronics.mycountries.feature.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abakan.electronics.data.CountriesRepository
import com.abakan.electronics.data.syncing.SyncingMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.abakan.electronics.data.Country as DataCountry

@HiltViewModel
class CountriesListViewModel @Inject constructor(
    repository: CountriesRepository,
    syncingMonitor: SyncingMonitor
) : ViewModel() {
    private val _displaySearchDialog = MutableStateFlow(false)
    val displaySearchDialog: StateFlow<Boolean> = _displaySearchDialog
    private val searchTerm = MutableStateFlow("")
    private var searchByName = true
    val uiState: StateFlow<CountriesListUIState> =
        repository
            .getCountries()
            .map { it.toUIState() }
            .combine(syncingMonitor.isSyncing) { state: CountriesListUIState, syncing: Boolean ->
                if (syncing) CountriesListUIState.Loading
                else state
            }
            .combine(searchTerm) { state: CountriesListUIState, searchTerm: String ->
                if (state is CountriesListUIState.Success && searchTerm.isNotEmpty()) {
                    val filteredCountries = state.countries.filter {
                        if (searchByName)
                            it.name.name.contains(
                                searchTerm,
                                true
                            )
                        else
                            it.capital.capital.contains(
                                searchTerm,
                                true
                            )
                    }
                    return@combine if (filteredCountries.isNotEmpty()) CountriesListUIState.Success(
                        filteredCountries
                    )
                    else CountriesListUIState.NoSearchResults
                }
                return@combine state
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                CountriesListUIState.Loading
            )

    private fun List<DataCountry>.toUIState(): CountriesListUIState =
        if (isNotEmpty()) {
            CountriesListUIState.Success(map {
                Country(Name(it.name), Capital(it.capital), CoatOfArmsImage(it.coatOfArmsUrl))
            })
        } else {
            CountriesListUIState.Loading
        }

    fun onSearchClick() {
        _displaySearchDialog.tryEmit(true)
    }

    fun onSearchCancel() {
        _displaySearchDialog.tryEmit(false)
    }

    fun onSearchPerform(term: String, searchByName: Boolean) {
        this.searchByName = searchByName
        _displaySearchDialog.tryEmit(false)
        searchTerm.tryEmit(term)
    }
}
