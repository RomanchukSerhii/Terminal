package com.serhiiromanchuk.terminal.presentation.stocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.serhiiromanchuk.terminal.domain.entity.Stock
import com.serhiiromanchuk.terminal.presentation.composables.LoadingScreen
import com.serhiiromanchuk.terminal.presentation.getApplicationComponent
import com.serhiiromanchuk.terminal.ui.theme.TerminalTheme

@Composable
fun StocksScreen(
    modifier: Modifier = Modifier,
    onStockItemClick: (ticker: String) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: StocksViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState()

    when (val currentsState = screenState.value) {
        is StocksScreenState.Content -> {
            MainScreenContent(
                modifier = modifier,
                stocks = currentsState.tickers,
                onStockItemClick = onStockItemClick
            )
        }

        StocksScreenState.Initial -> {}
        StocksScreenState.Loading -> {
            LoadingScreen(modifier = modifier)
        }
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    stocks: List<Stock>,
    onStockItemClick: (ticker: String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = stocks, key = { ticker -> ticker.ticker }) { ticker ->
            StockItem(stock = ticker, onStockItemClick = onStockItemClick )
        }
    }
}

@Composable
fun StockItem(
    modifier: Modifier = Modifier,
    stock: Stock,
    onStockItemClick: (ticker: String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onStockItemClick(stock.ticker) },
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
            Text(
                modifier = Modifier.weight(1f),
                text = stock.name
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stock.ticker,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTickerItem() {
    TerminalTheme {
        StockItem(stock = Stock(ticker = "AALP", name = "Apple Incorporation"), onStockItemClick = {})
    }
}