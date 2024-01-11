package com.serhiiromanchuk.terminal.domain.usecases

import com.serhiiromanchuk.terminal.domain.entity.Stock
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import javax.inject.Inject

class GetStocksListUseCase @Inject constructor(
    private val repository: TerminalRepository
) {
    suspend operator fun invoke(): List<Stock> {
        return repository.getStocksList()
    }
}