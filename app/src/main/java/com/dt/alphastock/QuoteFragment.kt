package com.dt.alphastock


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import com.dt.alphastock.network.StockApi
import com.dt.alphastock.network.StockRepository
import kotlinx.android.synthetic.main.fragment_quote.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext


class QuoteFragment : Fragment(), CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var httpClient: OkHttpClient

    private lateinit var retrofit: Retrofit

    private lateinit var stockApi: StockApi

    private lateinit var stockRepository: StockRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        httpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stockApi = retrofit.create(StockApi::class.java)
        stockRepository = StockRepository(stockApi)

        button_get_quote.setOnClickListener {
            //  the async way, using callbacks
//            launch {
//                val quote = stockApi.getQuote(symbol).await()
//
//                text_view_quote.text = quote.quote?.price?.roundTo(2)
//            }

            //  the sync way
            launch {
                val quote = async(Dispatchers.IO) { stockApi.getQuote(edit_text_symbol.text.toString()).execute() }

                text_view_quote.text = quote.await().body()?.quote?.price
            }

            //  the sync way, without async-await
//            launch {
//                val quote = withContext(Dispatchers.IO) {
//                    stockApi.getQuote(edit_text_symbol.text.toString()).execute()
//                }
//
//                text_view_quote.text = quote.body()?.quote?.price
//            }
        }

        button_start_stream.setOnClickListener {
            launch {
                val symbol = edit_text_symbol.text.toString()
                var previous = text_view_quote.text.toString().toFloatOrNull() ?: 0F
                stockRepository.getQuoteStream(symbol, this).consumeEach { globalQuote ->
                    globalQuote?.quote?.let {
                        val price = globalQuote.quote.price.roundTo(2)
                        if (price.toFloat() > previous) {
                            animateIncrease()
                        } else {
                            animateDecrease()
                        }
                        previous = price.toFloat()
                        text_view_quote.text = price
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = QuoteFragment()
    }

    private fun animateIncrease() {
        image_view_indicator.visibility = View.VISIBLE
        image_view_indicator.animate().rotation(0F).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun animateDecrease() {
        image_view_indicator.visibility = View.VISIBLE
        image_view_indicator.animate().rotation(180F).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }
}

fun String.roundTo(points: Int): String {
    val toFloat = this.toFloatOrNull()
    toFloat ?: return this
    return "%.${points}f".format(toFloat)
}
