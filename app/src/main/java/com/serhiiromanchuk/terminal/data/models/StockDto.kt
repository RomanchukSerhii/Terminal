package com.serhiiromanchuk.terminal.data.models

import com.google.gson.annotations.SerializedName

data class StockDto(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("name") val name: String
)
