package com.serhiiromanchuk.terminal.presentation.stocks

import com.serhiiromanchuk.terminal.domain.entity.Stock

sealed class StocksScreenState {
    data object Initial : StocksScreenState()

    data object Loading : StocksScreenState()

    data class Content(val tickers: List<Stock>) : StocksScreenState()
}