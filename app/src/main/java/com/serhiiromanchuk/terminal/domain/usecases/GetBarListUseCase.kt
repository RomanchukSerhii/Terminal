package com.serhiiromanchuk.terminal.domain.usecases

import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import com.serhiiromanchuk.terminal.presentation.diagram.TimeFrame
import javax.inject.Inject

class GetBarListUseCase @Inject constructor(
    private val repository: TerminalRepository
) {
    suspend operator fun invoke(
        timeFrame: TimeFrame,
        stocksTicker: String,
        startDate: String,
        endDate: String
    ): List<Bar> {
        return repository.getBarList(timeFrame, stocksTicker, startDate, endDate)
    }
}