package com.serhiiromanchuk.terminal.data.network

import com.serhiiromanchuk.terminal.data.models.ResultsResponseDto
import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/day/2023-01-09/2023-01-09?apiKey=nHyNZGJs2LwKi1tOk9U49IXbVtLm4XRM")
    suspend fun loadBars(): ResultsResponseDto
}