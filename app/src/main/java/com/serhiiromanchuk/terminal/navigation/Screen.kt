package com.serhiiromanchuk.terminal.navigation

import android.net.Uri

sealed class Screen(
    val route: String
) {

    data object Main : Screen (route = MAIN_SCREEN)

    data object Diagram : Screen (route = DIAGRAM_SCREEN) {
        private const val ROUTE_FOR_ARGS = "diagram_screen"

        fun getRouteWithArgs(ticker: String): String {
            return "$ROUTE_FOR_ARGS/$ticker"
        }
    }

    companion object {
        const val KEY_STOCKS_TICKER = "stocksTicker"
        private const val MAIN_SCREEN = "main_screen"
        private const val DIAGRAM_SCREEN = "diagram_screen/{$KEY_STOCKS_TICKER}"
    }
}