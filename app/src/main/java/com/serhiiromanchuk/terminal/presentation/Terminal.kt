package com.serhiiromanchuk.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.serhiiromanchuk.terminal.domain.entity.Bar
import kotlin.math.roundToInt


private const val MIN_VISIBLE_VALUE = 20

@Composable
fun Terminal(
    barList: List<Bar>
) {
    var visibleBarSize by remember {
        mutableIntStateOf(100)
    }

    val transformableState = TransformableState { zoomChange, _, _ ->
        visibleBarSize = (visibleBarSize / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_VALUE, barList.size)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .transformable(transformableState)
    ) {
        val barWidth = size.width / visibleBarSize
        val max = barList.maxOf { it.high }
        val min = barList.minOf { it.low }
        val pxPerPoint = size.height / (max - min)

        barList.take(visibleBarSize).forEachIndexed { index, bar ->
            val offsetX = size.width - (index * barWidth)
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                strokeWidth = 1f
            )
        }
    }
}