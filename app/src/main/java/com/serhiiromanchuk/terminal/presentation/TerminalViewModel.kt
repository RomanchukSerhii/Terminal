package com.serhiiromanchuk.terminal.presentation

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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("TerminalViewModel", "Exception caught: $throwable")
    }

    init {
        loadBars()
    }

    private fun loadBars() {
        viewModelScope.launch(exceptionHandler) {
            val barList = getBarListUseCase()
            _screenState.value = TerminalScreenState.Content(barList = barList)
        }
    }
}