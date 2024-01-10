package com.serhiiromanchuk.terminal.data.models

import com.google.gson.annotations.SerializedName

data class TickerDto(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("name") val name: String
)
