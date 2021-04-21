package com.kakadurf.catlas.data.di.module

import com.kakadurf.catlas.data.http.helper.HttpHelper
import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueriesToInterceptor
import com.kakadurf.catlas.data.http.helper.OPEN_STREETS_URL
import com.kakadurf.catlas.data.http.helper.TIMEOUT
import com.kakadurf.catlas.data.http.openstreetmap.RegionHttpRetriever
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val REGION_INTERCEPTOR_TAG = "region_format_interceptor"
const val REGION_CLIENT_TAG = "region_client"
const val REGION_RETROFIT_TAG = "region_retrofit"

@Module
class HttpRegionModule {
    @Provides
    @Named(REGION_INTERCEPTOR_TAG)
    fun formatInterceptor() = Interceptor {
        it.addQueriesToInterceptor(
            Pair("format", "geojson"),
            Pair("polygon_threshold", "0.1"),
            Pair("limit", "1"),
            Pair("email", "kakadurf@gmail.com"),
            Pair("polygon_geojson", "1")
        )
    }

    @Provides
    @Named(REGION_CLIENT_TAG)
    fun client(
        @Named(REGION_INTERCEPTOR_TAG)
        formatInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(formatInterceptor)
        .addInterceptor(HttpHelper.loggingInterceptor).build()

    @Provides
    @Named(REGION_RETROFIT_TAG)
    fun retrofit(@Named(REGION_CLIENT_TAG) client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(OPEN_STREETS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideService(
        @Named(REGION_RETROFIT_TAG)
        retrofit: Retrofit
    ): RegionHttpRetriever =
        retrofit.create(RegionHttpRetriever::class.java)
}