package com.serhiiromanchuk.terminal.presentation.diagram

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhiiromanchuk.terminal.R
import com.serhiiromanchuk.terminal.presentation.composables.LoadingScreen
import com.serhiiromanchuk.terminal.presentation.getApplicationComponent
import kotlin.math.roundToInt


private const val MIN_VISIBLE_VALUE = 20

@Composable
fun Diagram(
    modifier: Modifier = Modifier,
    ticker: String
) {
    val component = getApplicationComponent().getDiagramScreenComponent().create(ticker)
    val viewModel: DiagramViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()

    when (val currentState = screenState.value) {

        DiagramScreenState.Initial -> {}

        DiagramScreenState.Loading -> { LoadingScreen() }

        is DiagramScreenState.Content -> {
            val diagramState = rememberTerminalState(currentState.barList)
            DiagramContent(
                modifier = modifier,
                diagramState = diagramState,
                onDiagramStateChange = { diagramState.value = it },
                timeFrame = currentState.timeFrame
            )
            TimeFrames(
                selectedFrame = currentState.timeFrame,
                onTimeFrameClick = { viewModel.loadBars(it) })
        }
    }
}

@Composable
fun DiagramContent(
    modifier: Modifier = Modifier,
    diagramState: State<DiagramState>,
    onDiagramStateChange: (DiagramState) -> Unit,
    timeFrame: TimeFrame
) {
    Chart(
        modifier = modifier,
        diagramState = diagramState,
        onDiagramStateChange = onDiagramStateChange,
        timeFrame = timeFrame
    )

    diagramState.value.barList.firstOrNull()?.let {
        Prices(
            modifier = modifier,
            diagramState = diagramState,
            lastPrice = it.close
        )
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
    diagramState: State<DiagramState>,
    onDiagramStateChange: (DiagramState) -> Unit,
    timeFrame: TimeFrame
) {
    val currentState = diagramState.value
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarSize = (currentState.visibleBarSize / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_VALUE, currentState.barList.size)

        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceIn(
                0f,
                (currentState.barWidth * currentState.barList.size) - currentState.terminalWidth
            )
        onDiagramStateChange(
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
                onDiagramStateChange(
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
    diagramState: State<DiagramState>,
    lastPrice: Float
) {
    val currentState = diagramState.value
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