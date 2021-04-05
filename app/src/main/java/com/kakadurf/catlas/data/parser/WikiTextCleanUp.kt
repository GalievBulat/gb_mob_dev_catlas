package com.kakadurf.catlas.data.parser

class WikiTextCleanUp {
    private val cleanUpRegExp = "(\\<.*?ref.*\\?\\>+)*(\\{\\{.+?\\}\\})*"
    fun cleanupWikiText(rowText: String): String {
        return rowText.replace(cleanUpRegExp, "")
    }
}