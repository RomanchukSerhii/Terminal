package com.serhiiromanchuk.terminal.domain.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.Date

@Parcelize
@Immutable
data class Bar(
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float,
    val time: Long,
) : Parcelable {
    val calendar: Calendar
        get() {
            return Calendar.getInstance().apply {
                time = Date(this@Bar.time)
            }
        }
}
