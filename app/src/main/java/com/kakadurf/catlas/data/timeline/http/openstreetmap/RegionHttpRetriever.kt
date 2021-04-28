package com.kakadurf.catlas.data.timeline.http.openstreetmap

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface RegionHttpRetriever {
    @GET("/search")
    suspend fun searchForARegion(
        @Query("q") name: String
    ): ResponseBody
}
