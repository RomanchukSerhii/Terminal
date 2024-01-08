package com.serhiiromanchuk.terminal.domain.usecases

import com.serhiiromanchuk.terminal.domain.entity.Bar
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetBarListUseCase @Inject constructor(
    private val repository: TerminalRepository
) {
    operator fun invoke(): StateFlow<List<Bar>> {
        return repository.getBarList()
    }
}