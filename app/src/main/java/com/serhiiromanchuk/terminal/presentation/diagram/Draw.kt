package com.serhiiromanchuk.terminal.presentation.diagram

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhiiromanchuk.terminal.domain.entity.Bar
import java.util.Calendar
import java.util.Locale

fun DrawScope.drawPrices(
    maxPrice: Float,
    minPrice: Float,
    pxPerPoint: Float,
    lastPrice: Float,
    textMeasurer: TextMeasurer
) {
    // max price
    val maxPriceOffsetY = 0f
    drawDash(
        start = Offset(0f, maxPriceOffsetY),
        end = Offset(size.width, maxPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = maxPrice,
        offsetY = maxPriceOffsetY
    )

    // last price
    val lastPriceOffsetY = size.height - ((lastPrice - minPrice) * pxPerPoint)
    drawDash(
        start = Offset(0f, lastPriceOffsetY),
        end = Offset(size.width, lastPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = lastPrice,
        offsetY = lastPriceOffsetY
    )

    // min price
    val minPriceOffsetY = size.height
    drawDash(
        start = Offset(0f, minPriceOffsetY),
        end = Offset(size.width, minPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = minPrice,
        offsetY = minPriceOffsetY
    )
}

fun DrawScope.drawTimeDelimiter(
    bar: Bar,
    nextBar: Bar?,
    timeFrame: TimeFrame,
    offsetX: Float,
    textMeasurer: TextMeasurer
) {
    val calendar = bar.calendar

    if (!shouldDrawDelimiter(calendar, timeFrame, nextBar)) return

    drawDash(
        color = Color.White.copy(alpha = 0.5f),
        start = Offset(offsetX, 0f),
        end = Offset(offsetX, size.height),
    )

    val timeText = getTimeText(calendar, timeFrame)
    val textLayoutResult = textMeasurer.measure(
        text = timeText,
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(offsetX - (textLayoutResult.size.width / 2), size.height)
    )
}

fun DrawScope.drawDash(
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 1f
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                4.dp.toPx(), 4.dp.toPx()
            )
        )
    )
}

fun DrawScope.drawTextPrice(
    textMeasurer: TextMeasurer,
    price: Float,
    offsetY: Float
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width - textLayoutResult.size.width - 8.dp.toPx(), offsetY)
    )
}

private fun shouldDrawDelimiter(calendar: Calendar, timeFrame: TimeFrame, nextBar: Bar?): Boolean {
    val minutes = calendar.get(Calendar.MINUTE)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return when (timeFrame) {
        TimeFrame.MIN_5 -> minutes == 0

        TimeFrame.MIN_15 -> minutes == 0 && hours % 2 == 0

        TimeFrame.MIN_30, TimeFrame.HOUR -> {
            val nextBarDay = nextBar?.calendar?.get(Calendar.DAY_OF_MONTH)
            day != nextBarDay
        }
    }
}

private fun getTimeText(calendar: Calendar, timeFrame: TimeFrame): String {
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val nameOfMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

    return when (timeFrame) {
        TimeFrame.MIN_5, TimeFrame.MIN_15 -> {
            String.format("%02d:00", hours)
        }

        TimeFrame.MIN_30, TimeFrame.HOUR -> {
            String.format("%s %s", day, nameOfMonth)
        }
    }
}