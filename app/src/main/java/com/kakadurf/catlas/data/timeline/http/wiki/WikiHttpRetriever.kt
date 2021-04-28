package com.kakadurf.catlas.data.timeline.http.wiki

import retrofit2.http.GET
import retrofit2.http.Query

interface WikiHttpRetriever {
    @GET("/w/api.php")
    suspend fun getWikiPageSection(
        @Query("page") page: String,
        @Query("section") sectionNum: Int
    ): HttpWikiResponse

    @GET("/w/api.php")
    suspend fun getWholeWikiPage(
        @Query("page") page: String
    ): HttpWikiResponse
}
