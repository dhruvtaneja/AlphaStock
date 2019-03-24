package com.dt.alphastock.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query")
    fun getDayTimeSeries(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("apikey") apiKey: String = "UWXH52N9JQP0U2CZ"
    ): Call<TimeSeriesData>

    @GET("query")
    fun getQuote(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("apikey") apiKey: String = "UWXH52N9JQP0U2CZ"
    ): Call<GlobalQuote>

}
