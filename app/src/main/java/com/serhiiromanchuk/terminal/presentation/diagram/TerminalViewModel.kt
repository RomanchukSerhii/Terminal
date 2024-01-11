package com.serhiiromanchuk.terminal.presentation.diagram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.terminal.domain.usecases.GetBarListUseCase
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

    fun loadBars(timeFrame: TimeFrame = TimeFrame.HOUR, stocksTicker: String) {
        Log.d("TerminalVieModel", "ticker: $stocksTicker")
        lastScreenState = _screenState.value
        _screenState.value = TerminalScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val barList = getBarListUseCase(timeFrame, stocksTicker)
            _screenState.value = TerminalScreenState.Content(
                barList = barList,
                timeFrame = timeFrame
            )
        }
    }
}