package com.abakan.electronics.mycountries

import androidx.lifecycle.ViewModel
import com.abakan.electronics.data.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CountriesListViewModel @Inject constructor(private val repository: CountriesRepository): ViewModel() {
    val uiState: StateFlow<CountriesListUIState> = MutableStateFlow(CountriesListUIState.Loading)

}
