package com.dt.alphastock.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.resumeWithException

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            continuation.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                continuation.resumeWith(Result.success(response.body()!!))
            } else {
                continuation.resumeWith(Result.failure(Throwable(response.errorBody()!!.string())))
            }
        }
    })
    continuation.invokeOnCancellation { cancel() }
}
