package com.kakadurf.catlas.data.http.helper

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object HttpHelper {

    val loggingInterceptor = Interceptor {
        Log.d("hi", it.request().url().toString())
        it.proceed(it.request())
    }

    fun Interceptor.Chain.addQueryToInterceptor(query: String, value: String): Response {
        with(this) {
            return request().url().newBuilder()
                .addQueryParameter(query, value)
                .build().let { url ->
                    this.proceed(
                        request().newBuilder().url(url).build()
                    )
                }
        }
    }
}