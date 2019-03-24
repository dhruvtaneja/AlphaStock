package com.dt.alphastock.network

import com.google.gson.annotations.SerializedName



data class GlobalQuote(
    @SerializedName("Global Quote") val quote: Quote?
) {
    data class Quote(
        @SerializedName("01. symbol") val symbol: String,
        @SerializedName("05. price") val price: String
    )
}