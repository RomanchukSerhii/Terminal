package com.serhiiromanchuk.terminal.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiiromanchuk.terminal.navigation.AppNavGraph
import com.serhiiromanchuk.terminal.navigation.rememberNavigationState
import com.serhiiromanchuk.terminal.presentation.diagram.Diagram
import com.serhiiromanchuk.terminal.presentation.stocks.StocksScreen

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState()

    Scaffold {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            mainScreenContent = {
                StocksScreen(
                    modifier = modifier.padding(it),
                    onStockItemClick = { ticker ->
                        navigationState.navigateToDiagram(ticker)
                    }
                )
            },
            diagramScreenContent = { ticker ->
                Diagram(ticker = ticker)
            }
        )
    }
}

