package com.serhiiromanchuk.terminal.presentation.stocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.terminal.domain.entity.Stock
import com.serhiiromanchuk.terminal.domain.usecases.GetStocksListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StocksViewModel @Inject constructor(
    private val getTickerListUseCase: GetStocksListUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<StocksScreenState>(StocksScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    private var stockList = listOf<Stock>()

    init {
        loadingTickers()
    }

    private fun loadingTickers() {
        _screenState.value = StocksScreenState.Loading
        viewModelScope.launch {
            val stocks = getTickerListUseCase()
            stockList = stocks
            _screenState.value = StocksScreenState.Content(tickers = stocks)
        }
    }

    fun searchTickers(request: String) {
        val matchingStocks = stockList.filter { stock ->
            val isMatchName = stock.name.contains(request, ignoreCase = true)
            val isMatchTicker = stock.ticker.contains(request, ignoreCase = true)
            isMatchName || isMatchTicker
        }
        _screenState.value = StocksScreenState.Content(tickers = matchingStocks)
    }
}