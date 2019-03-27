package com.dt.alphastock.network

import retrofit2.Call

class StockRepository(private val stockApi: StockApi) {

    fun getQuoteCallable(symbol: String): Call<GlobalQuote> {
        return stockApi.getQuote(symbol = symbol)
    }

//    suspend fun getQuoteStream(symbol: String, coroutineScope: CoroutineScope): ReceiveChannel<GlobalQuote?> {
//        return coroutineScope.produce {
//            while (isActive) {
////                send(getQuote(symbol))
//                send(
//                    GlobalQuote(
//                    GlobalQuote.Quote(
//                        symbol,
//                        Random.nextInt(130, 160).toString()
//                    )
//                )
//                )
//                delay(3000)
//            }
//        }
//    }
}
