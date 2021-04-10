package com.kakadurf.catlas.data.http.wiki

import javax.inject.Inject


class WikiPageRepository @Inject constructor(
    val service: WikiHttpRetriever
) {
    suspend fun getWikiTextSection(page: String, section: Int): String {
        return service.getWikiPageSection(page, section).parse.wikiText.text
    }

    suspend fun getAllWikiTextFromPage(page: String): String {
        return service.getWholeWikiPage(page).parse.wikiText.text
    }
}