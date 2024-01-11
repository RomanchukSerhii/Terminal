package com.serhiiromanchuk.terminal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateToDiagram(ticker: String) {
        navHostController.navigate(Screen.Diagram.getRouteWithArgs(ticker))
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember { NavigationState(navHostController) }
}