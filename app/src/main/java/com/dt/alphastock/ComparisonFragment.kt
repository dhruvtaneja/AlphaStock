package com.dt.alphastock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dt.alphastock.network.StockApi
import kotlinx.android.synthetic.main.fragment_comparison.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class ComparisonFragment : Fragment(), CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var httpClient: OkHttpClient

    private lateinit var retrofit: Retrofit

    private lateinit var stockApi: StockApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comparison, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        httpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stockApi = retrofit.create(StockApi::class.java)

        button_compare.setOnClickListener {
            val symbol1 = edit_text_symbol_1.text.toString()
            val symbol2 = edit_text_symbol_2.text.toString()

            launch {
                val totalTime = measureTimeMillis {
                    val quotes = getQuotesAsync(symbol1, symbol2).await()

                    text_view_quote_1.text = quotes.first?.quote?.price?.roundTo(1)
                    text_view_quote_2.text = quotes.second?.quote?.price?.roundTo(1)
                }

                Log.d(TIME_QUOTE, "total time = $totalTime")
            }
        }
    }

    private fun getQuotesAsync(symbol1: String, symbol2: String) = async {
        val quote1 = async(Dispatchers.IO) { stockApi.getQuote(symbol1).execute() }
        val quote2 = async(Dispatchers.IO) { stockApi.getQuote(symbol2).execute() }

        quote1.await().body() to quote2.await().body()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ComparisonFragment()

        private const val TIME_QUOTE = "TIME_QUOTE"
    }
}
