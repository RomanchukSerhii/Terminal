package com.serhiiromanchuk.terminal.domain.entity

import androidx.compose.runtime.Immutable
import java.util.Calendar
import java.util.Date

@Immutable
data class Bar(
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float,
    val time: Long,
) {
    val calendar: Calendar
        get() {
            return Calendar.getInstance().apply {
                time = Date(this@Bar.time)
            }
        }
}
