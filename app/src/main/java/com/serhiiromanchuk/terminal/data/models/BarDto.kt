package com.serhiiromanchuk.terminal.data.models

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class BarDto(
    @SerializedName("o") val open: Float,
    @SerializedName("c") val close: Float,
    @SerializedName("h") val high: Float,
    @SerializedName("l") val low: Float,
    @SerializedName("t") val time: Long,
)
