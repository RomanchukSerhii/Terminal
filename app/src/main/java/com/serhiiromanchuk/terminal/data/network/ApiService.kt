package com.serhiiromanchuk.terminal.data.network

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import com.serhiiromanchuk.terminal.presentation.TimeFrame
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeframe}/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=nHyNZGJs2LwKi1tOk9U49IXbVtLm4XRM")
    suspend fun loadBars(
        @Path("timeframe") timeFrame: String
    ): ResultsResponseDto
}