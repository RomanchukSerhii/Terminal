package com.serhiiromanchuk.terminal.presentation.main

import com.serhiiromanchuk.terminal.domain.entity.Stock

sealed class MainScreenState {
    data object Initial : MainScreenState()

    data object Loading : MainScreenState()

    data class Content(val tickers: List<Stock>) : MainScreenState()
}