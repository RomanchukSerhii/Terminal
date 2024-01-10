package com.serhiiromanchuk.terminal.domain.repository

import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.entity.Ticker
import com.serhiiromanchuk.terminal.presentation.TimeFrame
import kotlinx.coroutines.flow.StateFlow

interface TerminalRepository {
    suspend fun getBarList(timeFrame: TimeFrame): List<Bar>

    suspend fun getTickerList(): List<Ticker>
}