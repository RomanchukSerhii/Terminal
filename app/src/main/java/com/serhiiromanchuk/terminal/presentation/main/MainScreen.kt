package com.serhiiromanchuk.terminal.presentation.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhiiromanchuk.terminal.domain.entity.Ticker
import com.serhiiromanchuk.terminal.presentation.composables.LoadingScreen
import com.serhiiromanchuk.terminal.presentation.getApplicationComponent
import com.serhiiromanchuk.terminal.ui.theme.TerminalTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val component = getApplicationComponent()
    val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()

    when (val currentsState = screenState.value) {
        is MainScreenState.Content -> {
            MainScreenContent(modifier = modifier, tickers = currentsState.tickers)
        }

        MainScreenState.Initial -> {}
        MainScreenState.Loading -> {
            LoadingScreen(modifier = modifier)
        }
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    tickers: List<Ticker>
) {
    LazyColumn(modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = tickers, key = { ticker -> ticker.ticker}) { ticker ->
            TickerItem(ticker = ticker)
        }
    }
}

@Composable
fun TickerItem(
    modifier: Modifier = Modifier,
    ticker: Ticker
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ticker.ticker, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ticker.name)
        }
    }
}

@Preview
@Composable
fun PreviewTickerItem() {
    TerminalTheme {
        TickerItem(ticker = Ticker(ticker = "AALP", name = "Apple Incorporation"))
    }
}