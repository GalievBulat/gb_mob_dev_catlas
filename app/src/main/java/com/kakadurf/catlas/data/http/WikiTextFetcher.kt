package com.kakadurf.catlas.data.http

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val WIKI_URL = "https://en.wikipedia.org/"
const val TIMEOUT = 10L

class WikiTextFetcher {
    suspend fun getWikiTextSection(page: String, section: Int) {
        Log.d("hi", service.getWikiPageSection(page, section).toString())
    }

    private val int = Interceptor {
        Log.d("hi", it.request().url().toString())
        it.proceed(it.request())
    }
    private val actionInterceptor = Interceptor {
        it.addQueryToInterceptor("action", "parse")
    }
    private val wikiTextInterceptor = Interceptor {
        it.addQueryToInterceptor("prop", "wikitext")
    }
    private val formatInterceptor = Interceptor {
        it.addQueryToInterceptor("format", "json")
    }
    private val client = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(actionInterceptor)
            .addInterceptor(formatInterceptor)
            .addInterceptor(wikiTextInterceptor)
            .addInterceptor(int).build()
    private val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(WIKI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val service: HttpRetriever = retrofit.create(
            HttpRetriever::class.java)

    private fun Interceptor.Chain.addQueryToInterceptor(query: String, value: String): Response {
        with(this) {
            return request().url().newBuilder()
                    .addQueryParameter(query, value)
                    .build().let { url ->
                        proceed(
                                request().newBuilder().url(url).build())
                    }
        }
    }
}