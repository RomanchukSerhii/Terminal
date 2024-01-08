package com.serhiiromanchuk.terminal.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhiiromanchuk.terminal.ui.theme.TerminalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val component = getApplicationComponent()
            val viewModel: TerminalViewModel = viewModel(factory = component.getViewModelFactory())
            val screenState = viewModel.screenState.collectAsState()
            TerminalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(val currentState = screenState.value) {
                        is TerminalScreenState.Initial -> {}

                        is TerminalScreenState.Content -> {
                            Terminal(barList = currentState.barList)
                        }
                    }
                }
            }
        }
    }
}