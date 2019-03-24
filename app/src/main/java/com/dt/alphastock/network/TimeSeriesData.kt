package com.dt.alphastock.network

import com.google.gson.annotations.SerializedName

data class TimeSeriesData(@SerializedName("Meta Data") val metaData: MetaData) {

    data class MetaData(@SerializedName("1. Information") val information: String)

}