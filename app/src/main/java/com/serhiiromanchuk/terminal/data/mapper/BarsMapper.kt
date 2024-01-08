package com.serhiiromanchuk.terminal.data.mapper

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import com.serhiiromanchuk.terminal.domain.entity.Bar

class BarsMapper {

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
}