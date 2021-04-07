package com.kakadurf.catlas.domain.wiki_parser

class WikiTextCleanUp {
    private val cleanUpRegExp = "(<.*?ref.*?>)*(\\{\\{.+?\\}\\})*".toRegex()
    fun cleanupWikiText(rowText: String): String {
        return rowText.replace(cleanUpRegExp, "")
    }

}