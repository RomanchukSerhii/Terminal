package com.serhiiromanchuk.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import com.serhiiromanchuk.terminal.domain.entity.Bar
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


private const val MIN_VISIBLE_VALUE = 20

@Composable
fun Terminal(
    barList: List<Bar>
) {
    var visibleBarSize by remember {
        mutableIntStateOf(100)
    }

    var terminalWidth by remember {
        mutableFloatStateOf(0f)
    }

    val barWidth by remember {
        derivedStateOf {
            terminalWidth / visibleBarSize
        }
    }

    var scrolledBy by remember {
        mutableFloatStateOf(0f)
    }

    val visibleBars by remember {
        derivedStateOf {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarSize).coerceAtMost(barList.size)
            barList.subList(startIndex, endIndex)
        }
    }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        visibleBarSize = (visibleBarSize / zoomChange ).roundToInt()
            .coerceIn(MIN_VISIBLE_VALUE, barList.size)

        scrolledBy = (scrolledBy + panChange.x)
            .coerceIn(0f, (barWidth * barList.size) - terminalWidth)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .transformable(transformableState)
    ) {
        terminalWidth = size.width
        val max = visibleBars.maxOf { it.high }
        val min = visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (max - min)

        translate(left = scrolledBy) {
            barList.forEachIndexed { index, bar ->
                val offsetX = size.width - (index * barWidth)
                val barColor = if (bar.close > bar.open) Color.Green else Color.Red
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
                    strokeWidth = barWidth / 2
                )
            }
        }
    }
}