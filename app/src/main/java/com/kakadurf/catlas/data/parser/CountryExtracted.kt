package com.kakadurf.catlas.data.parser

class CountryExtracted {
    private val preRegionDelimiter = " in "
    fun extractRegion(rowMessage: String): String {
        val wordSet = rowMessage.substringAfter(preRegionDelimiter).split(" ")
        var resultingWord = ""
        for (word in wordSet) {
            when {
                word.first().isUpperCase() -> {
                    resultingWord += word
                }
                word.matches("\\[\\[.*?]]".toRegex()) -> {
                    return word.removePrefix("[[").removeSuffix("]]")
                }
                else -> return resultingWord
            }
        }
        throw RuntimeException("wrong country format")
    }
}