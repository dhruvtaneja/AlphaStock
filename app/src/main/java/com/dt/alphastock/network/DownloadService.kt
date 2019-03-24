package com.dt.alphastock.network

import retrofit2.http.GET
import retrofit2.http.Streaming

interface DownloadService {

    @GET("https://sample-videos.com/xls/Sample-Spreadsheet-10000-rows.xls")
    @Streaming
    fun getFile()
}