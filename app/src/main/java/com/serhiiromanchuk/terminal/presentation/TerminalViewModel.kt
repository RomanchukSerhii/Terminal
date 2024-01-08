package com.serhiiromanchuk.terminal.presentation

import androidx.lifecycle.ViewModel
import com.serhiiromanchuk.terminal.domain.usecases.GetBarListUseCase
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    private val getBarListUseCase: GetBarListUseCase
) : ViewModel() {
}