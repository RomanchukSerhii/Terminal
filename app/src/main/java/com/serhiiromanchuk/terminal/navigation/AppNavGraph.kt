package com.serhiiromanchuk.terminal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    mainScreenContent: @Composable () -> Unit,
    diagramScreenContent: @Composable (String) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) { mainScreenContent() }
        composable(
            route = Screen.Diagram.route,
            arguments = listOf(
                navArgument(name = Screen.KEY_STOCKS_TICKER) {
                    type = NavType.StringType
                }
            )
        ) {
            val ticker = it.arguments?.getString(Screen.KEY_STOCKS_TICKER) ?: ""
            diagramScreenContent(ticker)
        }
    }
}