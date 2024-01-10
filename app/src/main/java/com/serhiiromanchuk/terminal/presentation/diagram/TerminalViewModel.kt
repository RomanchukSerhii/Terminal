package com.serhiiromanchuk.terminal.presentation.diagram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.terminal.domain.usecases.GetBarListUseCase
import com.serhiiromanchuk.terminal.domain.usecases.GetTickerListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    private val getBarListUseCase: GetBarListUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<TerminalScreenState>(TerminalScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenState.value = lastScreenState
    }

    private var lastScreenState: TerminalScreenState = TerminalScreenState.Initial

    init {
        loadBars()
    }

    fun loadBars(timeFrame: TimeFrame = TimeFrame.HOUR) {
        lastScreenState = _screenState.value
        _screenState.value = TerminalScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val barList = getBarListUseCase(timeFrame)
            _screenState.value = TerminalScreenState.Content(
                barList = barList,
                timeFrame = timeFrame
            )
        }
    }
}