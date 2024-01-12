package com.serhiiromanchuk.terminal.presentation.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.terminal.domain.usecases.GetBarListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiagramViewModel @Inject constructor(
    private val stocksTicker: String,
    private val getBarListUseCase: GetBarListUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<DiagramScreenState>(DiagramScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenState.value = lastScreenState
    }

    init {
        loadBars()
    }

    private var lastScreenState: DiagramScreenState = DiagramScreenState.Initial

    fun loadBars(timeFrame: TimeFrame = TimeFrame.HOUR) {
        lastScreenState = _screenState.value
        _screenState.value = DiagramScreenState.Loading

        viewModelScope.launch(exceptionHandler) {
            val barList = getBarListUseCase(timeFrame, stocksTicker)
            _screenState.value = DiagramScreenState.Content(
                barList = barList,
                timeFrame = timeFrame
            )
        }
    }
}