package com.serhiiromanchuk.terminal.presentation

import com.serhiiromanchuk.terminal.domain.entity.Bar

sealed class TerminalScreenState {
    data object Initial : TerminalScreenState()

    class Content(val barList: List<Bar>) : TerminalScreenState()
}