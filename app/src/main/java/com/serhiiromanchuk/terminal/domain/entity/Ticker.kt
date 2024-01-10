package com.serhiiromanchuk.terminal.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Ticker(
    val ticker: String,
    val name: String
)
