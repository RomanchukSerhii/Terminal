package com.serhiiromanchuk.terminal.domain.usecases

import com.serhiiromanchuk.terminal.domain.entity.Ticker
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import javax.inject.Inject

class GetTickerListUseCase @Inject constructor(
    private val repository: TerminalRepository
) {
    suspend operator fun invoke(): List<Ticker> {
        return repository.getTickerList()
    }
}