package com.kakadurf.catlas.data.di.module

import com.kakadurf.catlas.data.http.helper.*
import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueryToInterceptor
import com.kakadurf.catlas.data.http.openstreetmap.RegionHttpRetriever
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class HttpRegionModule {
    @Provides
    @Named(REGION_FORMAT_INT_TAG)
    fun formatInterceptor() = Interceptor {
        it.addQueryToInterceptor("format", "geojson")
    }

    @Provides
    @Named(REGION_GEOJSON_INT_TAG)
    fun geoJsonInterceptor() = Interceptor {
        it.addQueryToInterceptor("polygon_geojson", "1")
    }

    @Provides
    @Named(REGION_LIMIT_INT_TAG)
    fun limitInterceptor() = Interceptor {
        it.addQueryToInterceptor("limit", "1")
    }

    @Provides
    @Named(REGION_DETAILIZATION_TAG)
    fun detailizationInterceptor() = Interceptor {
        it.addQueryToInterceptor("polygon_threshold", "0.1")
    }

    @Provides
    @Named(REGION_EMAIL_TAG)
    fun emailInterceptor() = Interceptor {
        it.addQueryToInterceptor("email", "kakadurf@gmail.com")
    }

    @Provides
    @Named(REGION_CLIENT_TAG)
    fun client(
        @Named(REGION_FORMAT_INT_TAG)
        formatInterceptor: Interceptor,
        @Named(REGION_LIMIT_INT_TAG)
        limitInterceptor: Interceptor,
        @Named(REGION_GEOJSON_INT_TAG)
        geoJsonInterceptor: Interceptor,
        @Named(REGION_DETAILIZATION_TAG)
        detailizationInterceptor: Interceptor,
        @Named(REGION_EMAIL_TAG)
        emailInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(formatInterceptor)
        .addInterceptor(limitInterceptor)
        .addInterceptor(geoJsonInterceptor)
        .addInterceptor(detailizationInterceptor)
        .addInterceptor(emailInterceptor)
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