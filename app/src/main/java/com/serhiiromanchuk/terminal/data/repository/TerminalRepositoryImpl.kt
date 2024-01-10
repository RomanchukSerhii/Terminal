package com.serhiiromanchuk.terminal.data.repository

import com.serhiiromanchuk.terminal.data.mapper.TerminalMapper
import com.serhiiromanchuk.terminal.data.network.ApiService
import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.entity.Ticker
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import com.serhiiromanchuk.terminal.presentation.diagram.TimeFrame
import javax.inject.Inject

class TerminalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: TerminalMapper,
): TerminalRepository {
    override suspend fun getBarList(timeFrame: TimeFrame): List<Bar> {
        val timeFramePath = mapper.mapTimeFrameToPath(timeFrame)
        return mapper.mapResponseToBars(apiService.loadBars(timeFramePath))
    }

    override suspend fun getTickerList(): List<Ticker> {
        return mapper.mapTickerResponseToTickers(apiService.loadTickers())
    }
}