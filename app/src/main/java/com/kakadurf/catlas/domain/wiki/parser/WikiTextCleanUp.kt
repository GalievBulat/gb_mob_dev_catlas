package com.kakadurf.catlas.domain.wiki.parser
const val oldRexExp = "(<.*?ref.*?>)*(\\{\\{.+?\\}\\})*"
class WikiTextCleanUp {
    private val cleanUpRegExp = "<ref.*?>.*?</ref>".toRegex()
    fun cleanupWikiText(rowText: String): String {
        return rowText.replace(cleanUpRegExp, "")
    }
}
