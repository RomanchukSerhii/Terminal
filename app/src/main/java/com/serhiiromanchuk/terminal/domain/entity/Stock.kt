package com.serhiiromanchuk.terminal.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Stock(
    val ticker: String,
    val name: String
)
