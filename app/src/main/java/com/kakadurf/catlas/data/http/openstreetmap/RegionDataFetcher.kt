package com.kakadurf.catlas.data.http.openstreetmap

import android.util.Log
import com.kakadurf.catlas.data.http.helper.HttpHelper.addQueryToInterceptor
import com.kakadurf.catlas.data.http.wiki.TIMEOUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val OPEN_STREETS_URL = "https://nominatim.openstreetmap.org/"

class RegionDataFetcher {
    suspend fun getGeometries(name: String): JSONObject {
        Log.d("hi", "hi")
        return JSONObject(
            service.searchForARegion(name)
                .charStream().readText()
        )
    }

    private val int = Interceptor {
        Log.d("hi", it.request().url().toString())
        it.proceed(it.request())
    }
    private val formatInterceptor = Interceptor {
        it.addQueryToInterceptor("format", "geojson")
    }
    private val geoJsonInterceptor = Interceptor {
        it.addQueryToInterceptor("polygon_geojson", "1")
    }
    private val limitInterceptor = Interceptor {
        it.addQueryToInterceptor("limit", "1")
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(formatInterceptor)
        .addInterceptor(limitInterceptor)
        .addInterceptor(geoJsonInterceptor)
        .addInterceptor(int).build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(OPEN_STREETS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: RegionHttpRetriever = retrofit.create(
        RegionHttpRetriever::class.java
    )
}