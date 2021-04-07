package com.kakadurf.catlas.data.http.wiki

import android.util.Log
import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueryToInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class HttpWikiModule {
    @Singleton
    @Provides
    @Named("logW")
    fun getLogInt() = Interceptor {
        Log.d("hi", it.request().url().toString())
        it.proceed(it.request())
    }

    @Singleton
    @Provides
    @Named("act")
    fun getActionInterceptor() = Interceptor {
        it.addQueryToInterceptor("action", "parse")
    }

    @Singleton
    @Provides
    @Named("prop")
    fun getWikiTextInterceptor() = Interceptor {
        it.addQueryToInterceptor("prop", "wikitext")
    }

    @Singleton
    @Provides
    @Named("format")
    fun getFormatInterceptor() = Interceptor {
        it.addQueryToInterceptor("format", "json")
    }

    @Singleton
    @Provides
    fun getClient(
        actionInterceptor: Interceptor,
        formatInterceptor: Interceptor,
        wikiTextInterceptor: Interceptor,
        loggingInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(actionInterceptor)
        .addInterceptor(formatInterceptor)
        .addInterceptor(wikiTextInterceptor)
        .addInterceptor(loggingInterceptor).build()

    @Singleton
    @Provides
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(WIKI_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun getService(retrofit: Retrofit) = retrofit.create(
        WikiHttpRetriever::class.java
    )
}