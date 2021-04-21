package com.kakadurf.catlas.domain.wiki_parser
const val oldRexExp = "(<.*?ref.*?>)*(\\{\\{.+?\\}\\})*"
class WikiTextCleanUp {
    private val cleanUpRegExp = "<ref.*?>.*?</ref>".toRegex()
    fun cleanupWikiText(rowText: String): String {
        return rowText.replace(cleanUpRegExp, "")
    }

}