package com.serhiiromanchuk.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.serhiiromanchuk.terminal.domain.entity.Bar

@Composable
fun Terminal(
    barList: List<Bar>
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        val barWidth = size.width / barList.size
        barList.forEachIndexed { index, bar ->
            val max = barList.maxOf { it.high }
            val min = barList.minOf { it.low }
            val pxPerPoint = size.height / (max - min)
            val offsetX = index * barWidth
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                strokeWidth = 1f
            )
        }
    }
}