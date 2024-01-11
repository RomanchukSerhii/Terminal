package com.serhiiromanchuk.terminal.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.terminal.domain.usecases.GetStocksListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getTickerListUseCase: GetStocksListUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        loadingTickers()
    }

    private fun loadingTickers() {
        _screenState.value = MainScreenState.Loading
        viewModelScope.launch {
            val tickers = getTickerListUseCase()
            _screenState.value = MainScreenState.Content(tickers = tickers)
        }
    }
}