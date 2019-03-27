package com.dt.alphastock


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import com.dt.alphastock.network.GlobalQuote
import com.dt.alphastock.network.StockApi
import com.dt.alphastock.network.StockRepository
import kotlinx.android.synthetic.main.fragment_quote.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QuoteFragment : Fragment() {

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

            /*
            TODO
            1. Launch a coroutine with main context
            2. Switch context to I/O dispatcher
            3. Get the quote using retrofit call
            4. Set the price to the text view
             */

            /*
            TODO
            1. Launch a coroutine with main context
            2. Get quote using async builder
            3. Set price on the text view
             */

            stockApi.getQuote(edit_text_symbol.text.toString()).enqueue(object : Callback<GlobalQuote> {
                override fun onFailure(call: Call<GlobalQuote>, t: Throwable) {
                    //  show some error
                }

                override fun onResponse(call: Call<GlobalQuote>, response: Response<GlobalQuote>) {
                    text_view_quote.text = response.body()?.quote?.price
                }
            })

            /*
            TODO
            1. Get the quote using a suspendCancellableCoroutine
            2. Set price on the text view
             */

        }

        button_start_stream.setOnClickListener {
            /*
            TODO
            1. Start a coroutine
            2. Get stock price stream
            3. Update the price on the text view
             */
        }
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
