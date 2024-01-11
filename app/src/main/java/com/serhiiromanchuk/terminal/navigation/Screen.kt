package com.serhiiromanchuk.terminal.navigation

sealed class Screen(
    val route: String
) {

    data object Main : Screen (route = MAIN_SCREEN)

    data object Diagram : Screen (route = DIAGRAM_SCREEN)

    companion object {
        private const val MAIN_SCREEN = "main_screen"
        private const val DIAGRAM_SCREEN = "diagram_screen"
    }
}