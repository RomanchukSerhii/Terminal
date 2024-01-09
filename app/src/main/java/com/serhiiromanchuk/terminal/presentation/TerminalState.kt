package com.serhiiromanchuk.terminal.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.serhiiromanchuk.terminal.domain.entity.Bar
import kotlin.math.roundToInt

data class TerminalState(
    val barList: List<Bar>,
    val visibleBarSize: Int = 100,
    val terminalWidth: Float = 0f,
    val scrolledBy: Float = 0f
) {
    val barWidth: Float
        get() = terminalWidth / visibleBarSize

    val visibleBars: List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarSize).coerceAtMost(barList.size)
            return barList.subList(startIndex, endIndex)
        }

    companion object {
        val Saver: Saver<MutableState<TerminalState>, *> = Saver(
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
                val terminalState = TerminalState(
                    barList = values["barList"] as List<Bar>,
                    visibleBarSize = values["visibleBarSize"] as Int,
                    terminalWidth = values["terminalWidth"]  as Float,
                    scrolledBy = values["scrolledBy"] as Float
                )
                mutableStateOf(terminalState)
            }
        )
    }
}

@Composable
fun rememberTerminalState(barList: List<Bar>): MutableState<TerminalState> {
    return rememberSaveable(saver = TerminalState.Saver) {
        mutableStateOf(TerminalState(barList))
    }
}
