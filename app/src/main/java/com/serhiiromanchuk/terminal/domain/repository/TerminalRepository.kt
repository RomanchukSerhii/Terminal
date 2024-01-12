package com.serhiiromanchuk.terminal.domain.repository

import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.entity.Stock
import com.serhiiromanchuk.terminal.presentation.diagram.TimeFrame

interface TerminalRepository {
    suspend fun getBarList(
        timeFrame: TimeFrame,
        stocksTicker: String,
        startDate: String,
        endDate: String
    ): List<Bar>

    suspend fun getStocksList(): List<Stock>
}