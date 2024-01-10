package com.serhiiromanchuk.terminal.data.models

import com.google.gson.annotations.SerializedName

data class TickerResponseDto(
    @SerializedName("results") val tickerList: List<TickerDto>
)
