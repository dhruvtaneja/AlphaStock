package com.dt.alphastock.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import retrofit2.Call
import kotlin.random.Random

class StockRepository(private val stockApi: StockApi) {

    fun getQuoteCallable(symbol: String): Call<GlobalQuote> {
        return stockApi.getQuote(symbol = symbol)
    }

    private suspend fun getQuote(symbol: String): GlobalQuote {
        return stockApi.getQuote(symbol).await()
    }

    suspend fun getQuoteStream(symbol: String, coroutineScope: CoroutineScope): ReceiveChannel<GlobalQuote?> {
        return coroutineScope.produce {
            while (isActive) {
//                send(getQuote(symbol))
                send(
                    GlobalQuote(
                    GlobalQuote.Quote(
                        symbol,
                        Random.nextInt(130, 160).toString()
                    )
                )
                )
                delay(3000)
            }
        }
    }
}
