package com.kakadurf.catlas.data.timeline.module

import com.kakadurf.catlas.BuildConfig
import com.kakadurf.catlas.data.timeline.http.openstreetmap.RegionHttpRetriever
import com.kakadurf.catlas.data.timeline.http.service.addQueriesToInterceptor
import com.kakadurf.catlas.data.timeline.http.service.loggingInterceptor
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
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
const val TIMEOUT = 60L
@Module
class HttpRegionModule {
    @Provides
    @MapScope
    @Named(REGION_INTERCEPTOR_TAG)
    fun formatInterceptor() = Interceptor {
        it.addQueriesToInterceptor(
            "format" to "geojson",
            "polygon_threshold" to "0.1",
            "limit" to "1",
            "email" to "kakadurf@gmail.com",
            "polygon_geojson" to "1"
        )
    }

    @Provides
    @MapScope
    @Named(REGION_CLIENT_TAG)
    fun client(
        @Named(REGION_INTERCEPTOR_TAG)
        formatInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(formatInterceptor)
        .addInterceptor(loggingInterceptor).build()

    @Provides
    @MapScope
    @Named(REGION_RETROFIT_TAG)
    fun retrofit(@Named(REGION_CLIENT_TAG) client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.OPENSTREETS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @MapScope
    fun provideService(
        @Named(REGION_RETROFIT_TAG)
        retrofit: Retrofit
    ): RegionHttpRetriever =
        retrofit.create(RegionHttpRetriever::class.java)
}
