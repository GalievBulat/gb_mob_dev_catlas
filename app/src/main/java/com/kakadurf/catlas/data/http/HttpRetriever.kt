package com.kakadurf.catlas.data.http

import retrofit2.http.GET
import retrofit2.http.Query

interface HttpRetriever {
    @GET("/w/api.php")
    suspend fun getWikiPageSection(
        @Query("page") page: String,
        @Query("section") sectionNum: Int
    ): HttpWikiResponse
}