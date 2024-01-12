package com.serhiiromanchuk.terminal.presentation.diagram

import com.serhiiromanchuk.terminal.domain.entity.Bar

sealed class DiagramScreenState {
    data object Initial : DiagramScreenState()

    data object Loading : DiagramScreenState()

    class Content(val barList: List<Bar>, val timeFrame: TimeFrame) : DiagramScreenState()
}