package com.serhiiromanchuk.terminal.data.mapper

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import com.serhiiromanchuk.terminal.data.models.TickerResponseDto
import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.entity.Ticker
import com.serhiiromanchuk.terminal.presentation.diagram.TimeFrame
import javax.inject.Inject

class TerminalMapper @Inject constructor() {

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

    fun mapTickerResponseToTickers(responseDto: TickerResponseDto): List<Ticker> {
        val result = mutableListOf<Ticker>()

        responseDto.tickerList.forEach {
            val ticker = Ticker(
                ticker = it.ticker,
                name = it.name
            )
            result.add(ticker)
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