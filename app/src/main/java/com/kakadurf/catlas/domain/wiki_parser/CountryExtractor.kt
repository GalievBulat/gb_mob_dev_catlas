package com.kakadurf.catlas.domain.wiki_parser

class CountryExtractor {
    private val preRegionDelimiter = " in "
    fun extractRegion(rowMessage: String): String? {
        val wordSet = rowMessage.substringAfter(preRegionDelimiter, "")
            .split(" ")
        var resultingWord: String? = null
        for (word in wordSet) {
            if (word.isNotEmpty()) {
                when {
                    word.first().isUpperCase() -> {
                        //?
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
        return resultingWord
        //throw RuntimeException("wrong country format")
    }
}