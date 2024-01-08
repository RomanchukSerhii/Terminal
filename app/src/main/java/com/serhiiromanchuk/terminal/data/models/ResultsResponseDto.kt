package com.serhiiromanchuk.terminal.data.models

import com.google.gson.annotations.SerializedName

data class ResultsResponseDto(
    @SerializedName("results") val barList: List<BarDto>
)
