package com.kakadurf.catlas.data.http.wiki

import javax.inject.Inject

const val WIKI_URL = "https://en.wikipedia.org/"
const val TIMEOUT = 10L

class WikiPageRepository @Inject constructor(val service: WikiHttpRetriever) {
    suspend fun getWikiTextSection(page: String, section: Int): String {
        return service.getWikiPageSection(page, section).parse.wikiText.text
    }


}