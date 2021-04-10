package com.kakadurf.catlas.data.di.module

import com.kakadurf.catlas.data.http.helper.*
import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueryToInterceptor
import com.kakadurf.catlas.data.http.helper.HttpHelper.loggingInterceptor
import com.kakadurf.catlas.data.http.wiki.WikiHttpRetriever
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class HttpWikiModule {
    @Provides
    @Named(WIKI_ACTION_INT_TAG)
    fun getActionInterceptor() = Interceptor {
        it.addQueryToInterceptor("action", "parse")
    }

    @Provides
    @Named(WIKI_PROPERTY_INT_TAG)
    fun getWikiTextInterceptor() = Interceptor {
        it.addQueryToInterceptor("prop", "wikitext")
    }

    @Provides
    @Named(WIKI_FORMAT_INT_TAG)
    fun getFormatInterceptor() = Interceptor {
        it.addQueryToInterceptor("format", "json")
    }

    @Provides
    @Named(WIKI_CLIENT_TAG)
    fun getClient(
        @Named(WIKI_ACTION_INT_TAG)
        actionInterceptor: Interceptor,
        @Named(WIKI_FORMAT_INT_TAG)
        formatInterceptor: Interceptor,
        @Named(WIKI_PROPERTY_INT_TAG)
        wikiTextInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(actionInterceptor)
        .addInterceptor(formatInterceptor)
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