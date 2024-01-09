package com.serhiiromanchuk.terminal.data.mapper

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.presentation.TimeFrame
import javax.inject.Inject

class BarsMapper @Inject constructor() {

    fun mapResponseToBars(responseDto: ResultsResponseDto): List<Bar> {
        val result = mutableListOf<Bar>()

        responseDto.barList.forEach {
            val bar = Bar(
                open = it.open,
                close = it.close,
                high = it.high,
                low = it.low,
                time = it.time
            )
            result.add(bar)
        }

        return result
    }

    fun mapTimeFrameToPath(timeFrame: TimeFrame): String {
        return when(timeFrame) {
            TimeFrame.MIN_5 -> "5/minute"
            TimeFrame.MIN_15 -> "15/minute"
            TimeFrame.MIN_30 -> "30/minute"
            TimeFrame.HOUR -> "1/hour"
        }
    }
}