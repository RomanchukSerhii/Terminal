package com.serhiiromanchuk.terminal.presentation

import com.serhiiromanchuk.terminal.domain.entity.Bar

sealed class TerminalScreenState {
    data object Initial : TerminalScreenState()

    data object Loading : TerminalScreenState()

    class Content(val barList: List<Bar>, val timeFrame: TimeFrame) : TerminalScreenState()
}