package com.serhiiromanchuk.terminal.presentation.diagram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.serhiiromanchuk.terminal.domain.entity.Bar
import kotlin.math.roundToInt

data class DiagramState(
    val barList: List<Bar>,
    val visibleBarSize: Int = 40,
    val terminalWidth: Float = 1f,
    val terminalHeight: Float = 1f,
    val scrolledBy: Float = 0f
) {
    val barWidth: Float
        get() = terminalWidth / visibleBarSize

    private val visibleBars: List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarSize).coerceAtMost(barList.size)
            return barList.subList(startIndex, endIndex)
        }

    val max
        get() = visibleBars.maxOf { it.high }
    val min
        get() = visibleBars.minOf { it.low }
    val pxPerPoint
        get() = terminalHeight / (max - min)

    companion object {
        val Saver: Saver<MutableState<DiagramState>, *> = Saver(
            save = {
                val terminalState = it.value
                mapOf(
                    "barList" to terminalState.barList,
                    "visibleBarSize" to terminalState.visibleBarSize,
                    "terminalWidth" to terminalState.terminalWidth,
                    "scrolledBy" to terminalState.scrolledBy
                )
            },
            restore = { values ->
                val terminalState = DiagramState(
                    barList = values["barList"] as List<Bar>,
                    visibleBarSize = values["visibleBarSize"] as Int,
                    terminalWidth = values["terminalWidth"] as Float,
                    scrolledBy = values["scrolledBy"] as Float
                )
                mutableStateOf(terminalState)
            }
        )
    }
}

@Composable
fun rememberTerminalState(barList: List<Bar>): MutableState<DiagramState> {
    return rememberSaveable(saver = DiagramState.Saver) {
        mutableStateOf(DiagramState(
            barList = barList,
            visibleBarSize = if (barList.size > 40) 40 else barList.size
        ))
    }
}
