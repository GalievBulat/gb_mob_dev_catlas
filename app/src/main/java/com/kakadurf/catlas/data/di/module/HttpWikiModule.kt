package com.kakadurf.catlas.data.di.module

import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueriesToInterceptor
import com.kakadurf.catlas.data.http.helper.HttpHelper.loggingInterceptor
import com.kakadurf.catlas.data.http.helper.TIMEOUT
import com.kakadurf.catlas.data.http.helper.WIKI_URL
import com.kakadurf.catlas.data.http.wiki.WikiHttpRetriever
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val WIKI_INTERCEPTOR_TAG = "wiki_property_interceptor"
const val WIKI_CLIENT_TAG = "wiki_client"
const val WIKI_RETROFIT_TAG = "wiki_retrofit"

@Module
class HttpWikiModule {
    @Provides
    @Named(WIKI_INTERCEPTOR_TAG)
    fun getWikiTextInterceptor() = Interceptor {
        it.addQueriesToInterceptor(
            Pair("prop", "wikitext"),
            Pair("format", "json"),
            Pair("action", "parse")
        )

    }


    @Provides
    @Named(WIKI_CLIENT_TAG)
    fun getClient(
        @Named(WIKI_INTERCEPTOR_TAG)
        wikiTextInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(wikiTextInterceptor)
        .addInterceptor(loggingInterceptor).build()

    @Provides
    @Named(WIKI_RETROFIT_TAG)
    fun getRetrofit(@Named(WIKI_CLIENT_TAG) client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(WIKI_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun getService(@Named(WIKI_RETROFIT_TAG) retrofit: Retrofit) = retrofit.create(
        WikiHttpRetriever::class.java
    )
}