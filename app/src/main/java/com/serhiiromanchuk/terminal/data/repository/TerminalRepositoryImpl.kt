package com.serhiiromanchuk.terminal.data.repository

import com.serhiiromanchuk.terminal.data.mapper.BarsMapper
import com.serhiiromanchuk.terminal.data.network.ApiService
import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import com.serhiiromanchuk.terminal.presentation.TimeFrame
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TerminalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: BarsMapper
): TerminalRepository {
    override suspend fun getBarList(timeFrame: TimeFrame): List<Bar> {
        val timeFramePath = mapper.mapTimeFrameToPath(timeFrame)
        return mapper.mapResponseToBars(apiService.loadBars(timeFramePath))
    }
}