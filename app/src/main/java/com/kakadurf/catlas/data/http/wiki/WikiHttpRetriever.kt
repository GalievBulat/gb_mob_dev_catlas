package com.kakadurf.catlas.data.http.wiki

import retrofit2.http.GET
import retrofit2.http.Query

interface WikiHttpRetriever {
    @GET("/w/api.php")
    suspend fun getWikiPageSection(
        @Query("page") page: String,
        @Query("section") sectionNum: Int
    ): HttpWikiResponse
}