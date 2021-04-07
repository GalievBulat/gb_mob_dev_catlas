package com.kakadurf.catlas.domain.wiki_parser

class DateConverter {
    fun extractYear(rowDate: String): Int? {
        var year: Int? = null
        val words = rowDate.split(" ")
        For@ for (i in words.indices) {
            if (words[i].matches("\\d+".toRegex())) {
                year = words[i].toInt()
                continue@For
            } else if (with(words[i]) {
                    contains("\\d+".toRegex()) && (
                            endsWith("th") || endsWith("st") ||
                                    endsWith("nd") || endsWith("rd"))
                }) {
                year = words[i].takeWhile { it.isDigit() }.toInt()
            } else if (words[i].matches("\\d+-\\d+".toRegex())) {
                year = words[i].takeWhile { it.isDigit() }.toInt()
            }
            if (year != null) {
                when (words[i]) {
                    "Mya" -> year *= -1000000
                    "kya", "ka" -> year *= -1000
                    "BC" -> year *= -1
                    //"AD" -> year
                    "century" -> year *= 100
                }
            }
        }
        return year
        /*
        year?.let { return it } ?: throw RuntimeException("wrong data format")*/
    }
}