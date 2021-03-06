package com.kakadurf.catlas.domain.wiki.parser

import javax.inject.Inject

class
CountryExtractor @Inject constructor() {
    private val preRegionDelimiter = " in "
    fun extractRegion(rowMessage: String): String? {
        val wordSet = rowMessage.substringAfter(preRegionDelimiter, "")
            .split(" ")
        var resultingWord: String? = null
        for (word in wordSet) {
            if (word.isNotEmpty()) {
                when {
                    word.first().isUpperCase() -> {
                        // ?
                        val newWord = word.dropLastWhile {
                            !it.isLetter()
                        }
                        resultingWord = (resultingWord ?: "").plus(" $newWord")
                        if (word.endsWith(".")) {
                            return resultingWord
                        }
                    }
                    word.matches("\\[\\[.*?]]".toRegex()) -> {
                        return word.removePrefix("[[").removeSuffix("]]")
                    }
                    else -> return resultingWord
                }
            }
        }
        return resultingWord?.trim()
    }
}
