package com.dt.alphastock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dt.alphastock.network.StockApi
import kotlinx.android.synthetic.main.fragment_comparison.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

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
            /*
            TODO
            1. Get symbols from the edit texts
            2. Get quotes in parallel using async await
            3. Round the prices to 1 decimal point
            4. Set prices to the text views
             */
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ComparisonFragment()
    }
}
