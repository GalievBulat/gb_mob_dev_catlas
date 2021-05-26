package com.kakadurf.catlas.data.timeline.http.extentions

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

val loggingInterceptor = Interceptor {
    Log.d("hi", it.request().url().toString())
    it.proceed(it.request())
}

fun Interceptor.Chain.addQueriesToInterceptor(vararg queryPairs: Pair<String, String>): Response {
    with(this) {
        val requestBuilder = request().url().newBuilder()
        queryPairs.forEach {
            requestBuilder.addQueryParameter(it.first, it.second)
        }
        return requestBuilder.build().let { url ->
            this.proceed(request().newBuilder().url(url).build())
        }
    }
}
