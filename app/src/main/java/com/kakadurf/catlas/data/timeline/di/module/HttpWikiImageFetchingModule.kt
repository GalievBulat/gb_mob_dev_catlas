package com.kakadurf.catlas.data.timeline.di.module

import com.kakadurf.catlas.BuildConfig
import com.kakadurf.catlas.data.timeline.http.extentions.addQueriesToInterceptor
import com.kakadurf.catlas.data.timeline.http.extentions.loggingInterceptor
import com.kakadurf.catlas.data.timeline.http.wiki.WikiImageHttpRetrofit
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val WIKI_IMAGE_INTERCEPTOR_TAG = "wiki_image_property_interceptor"
const val WIKI_IMAGE_CLIENT_TAG = "wiki_image_client"
const val WIKI_IMAGE_RETROFIT_TAG = "wiki_image_retrofit"
const val WIKI_IMAGE_TIMEOUT = 30L

@Module
class HttpWikiImageFetchingModule {
    @Provides
    @MapScope
    @Named(WIKI_IMAGE_INTERCEPTOR_TAG)
    fun getWikiTextInterceptor() = Interceptor {
        it.addQueriesToInterceptor(
            "prop" to "imageinfo",
            "iiprop" to "url",
            "format" to "json",
            "action" to "query"
        )
    }

    @Provides
    @MapScope
    @Named(WIKI_IMAGE_CLIENT_TAG)
    fun getClient(
        @Named(WIKI_INTERCEPTOR_TAG)
        wikiTextInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(WIKI_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(wikiTextInterceptor)
        .addInterceptor(loggingInterceptor).build()

    @Provides
    @MapScope
    @Named(WIKI_IMAGE_RETROFIT_TAG)
    fun getRetrofit(@Named(WIKI_CLIENT_TAG) client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.WIKI_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @MapScope
    fun getService(@Named(WIKI_RETROFIT_TAG) retrofit: Retrofit) = retrofit.create(
        WikiImageHttpRetrofit::class.java
    )
}
