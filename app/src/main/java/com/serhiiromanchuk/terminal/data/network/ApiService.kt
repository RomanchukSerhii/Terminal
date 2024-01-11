package com.serhiiromanchuk.terminal.data.network

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import com.serhiiromanchuk.terminal.data.models.TickerResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("v2/aggs/ticker/{stocksTicker}/range/{timeframe}/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=5000&apiKey=nHyNZGJs2LwKi1tOk9U49IXbVtLm4XRM")
    suspend fun loadBars(
        @Path("timeframe") timeFrame: String,
        @Path("stocksTicker") stocksTicker: String
    ): ResultsResponseDto


    @GET("v3/reference/tickers?market=stocks&active=true&order=asc&sort=primary_exchange&apiKey=nHyNZGJs2LwKi1tOk9U49IXbVtLm4XRM")
    suspend fun loadTickers(): TickerResponseDto
}