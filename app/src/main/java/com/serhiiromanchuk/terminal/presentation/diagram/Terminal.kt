package com.serhiiromanchuk.terminal.presentation.diagram

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhiiromanchuk.terminal.R
import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.presentation.getApplicationComponent
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


private const val MIN_VISIBLE_VALUE = 20

@Composable
fun Terminal(
    modifier: Modifier = Modifier
) {
    val component = getApplicationComponent()
    val viewModel: TerminalViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()

    when (val currentState = screenState.value) {

        TerminalScreenState.Initial -> {}

        TerminalScreenState.Loading -> {
            LoadingScreen()
        }

        is TerminalScreenState.Content -> {
            val terminalState = rememberTerminalState(currentState.barList)
            TerminalContent(
                modifier = modifier,
                terminalState = terminalState,
                onTerminalStateChange = { terminalState.value = it },
                timeFrame = currentState.timeFrame
            )
            TimeFrames(
                selectedFrame = currentState.timeFrame,
                onTimeFrameClick = { viewModel.loadBars(it) })
        }
    }
}

@Composable
fun TerminalContent(
    modifier: Modifier = Modifier,
    terminalState: State<TerminalState>,
    onTerminalStateChange: (TerminalState) -> Unit,
    timeFrame: TimeFrame
) {
    Chart(
        modifier = modifier,
        terminalState = terminalState,
        onTerminalStateChange = onTerminalStateChange,
        timeFrame = timeFrame
    )

    terminalState.value.barList.firstOrNull()?.let {
        Prices(
            modifier = modifier,
            terminalState = terminalState,
            lastPrice = it.close
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun TimeFrames(
    modifier: Modifier = Modifier,
    selectedFrame: TimeFrame,
    onTimeFrameClick: (TimeFrame) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeFrame.entries.forEach { timeFrame ->
            val labelResId = when (timeFrame) {
                TimeFrame.MIN_5 -> R.string.timeframe_5_minutes
                TimeFrame.MIN_15 -> R.string.timeframe_15_minutes
                TimeFrame.MIN_30 -> R.string.timeframe_30_minutes
                TimeFrame.HOUR -> R.string.timeframe_1_hours
            }
            val isSelected = selectedFrame == timeFrame
            AssistChip(
                onClick = { onTimeFrameClick(timeFrame) },
                label = { Text(text = stringResource(id = labelResId)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) Color.White else Color.Black,
                    labelColor = if (isSelected) Color.Black else Color.White
                )
            )
        }
    }
}

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    terminalState: State<TerminalState>,
    onTerminalStateChange: (TerminalState) -> Unit,
    timeFrame: TimeFrame
) {
    val currentState = terminalState.value
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarSize = (currentState.visibleBarSize / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_VALUE, currentState.barList.size)

        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceIn(
                0f,
                (currentState.barWidth * currentState.barList.size) - currentState.terminalWidth
            )
        onTerminalStateChange(
            currentState.copy(
                visibleBarSize = visibleBarSize,
                scrolledBy = scrolledBy
            )
        )
    }
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .clipToBounds()
            .padding(
                top = 32.dp,
                bottom = 32.dp,
                end = 32.dp
            )
            .transformable(transformableState)
            .onSizeChanged {
                onTerminalStateChange(
                    currentState.copy(
                        terminalWidth = it.width.toFloat(),
                        terminalHeight = it.height.toFloat()
                    )
                )
            }
    ) {
        val min = currentState.min
        val pxPerPoint = currentState.pxPerPoint

        translate(left = currentState.scrolledBy) {
            currentState.barList.forEachIndexed { index, bar ->
                val offsetX = size.width - (index * currentState.barWidth)
                val barColor = if (bar.close > bar.open) Color.Green else Color.Red
                drawTimeDelimiter(
                    bar = bar,
                    nextBar = if (index < currentState.barList.size - 1) {
                        currentState.barList[index + 1]
                    } else null,
                    timeFrame = timeFrame,
                    offsetX = offsetX,
                    textMeasurer = textMeasurer
                )
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                    strokeWidth = 1f
                )
                drawLine(
                    color = barColor,
                    start = Offset(offsetX, size.height - ((bar.open - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - min) * pxPerPoint)),
                    strokeWidth = currentState.barWidth / 2
                )
            }
        }
    }
}

@Composable
fun Prices(
    modifier: Modifier = Modifier,
    terminalState: State<TerminalState>,
    lastPrice: Float
) {
    val currentState = terminalState.value
    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .padding(vertical = 32.dp)
    ) {
        drawPrices(
            maxPrice = currentState.max,
            minPrice = currentState.min,
            pxPerPoint = currentState.pxPerPoint,
            lastPrice = lastPrice,
            textMeasurer = textMeasurer
        )
    }
}


private fun DrawScope.drawPrices(
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

private fun DrawScope.drawTimeDelimiter(
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

private fun DrawScope.drawDash(
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

private fun DrawScope.drawTextPrice(
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