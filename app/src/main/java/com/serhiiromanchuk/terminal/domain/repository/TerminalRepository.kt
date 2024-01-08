package com.serhiiromanchuk.terminal.domain.repository

import com.serhiiromanchuk.terminal.domain.entity.Bar
import kotlinx.coroutines.flow.StateFlow

interface TerminalRepository {
    suspend fun getBarList(): List<Bar>
}