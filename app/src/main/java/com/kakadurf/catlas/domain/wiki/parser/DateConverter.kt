package com.kakadurf.catlas.domain.wiki.parser

import javax.inject.Inject
import kotlin.math.roundToInt

class DateConverter @Inject constructor() {
    fun extractYear(rowDate: String): Int? {
        var year: Double? = null
        val words = rowDate.split(" ")
        For@ for (i in words.indices) {
            if (words[i].matches("\\d+(\\.\\d+)*".toRegex())) {
                year = words[i].toDouble()
                continue@For
            } else if (with(words[i]) {
                    contains("\\d+".toRegex()) && (
                            endsWith("th") || endsWith("st") ||
                                    endsWith("nd") || endsWith("rd")
                            )
                }
            ) {
                year = words[i].takeWhile { it.isDigit() }.toDouble()
            } else if (words[i].matches("(\\d+(\\.\\d+)*-)(\\d+(\\.\\d+)*)+".toRegex())) {
                year = words[i].takeWhile { it.isDigit() || it == '.' }.toDouble()
            }
            if (year != null) {
                when (words[i]) {
                    "Mya", "mya" -> year *= -1000000
                    "kya", "ka" -> year *= -1000
                    "BC" -> year *= -1
                    // "AD" -> year
                    "century" -> year *= 100
                }
            }
        }
        return year?.roundToInt()
        /*
        year?.let { return it } ?: throw RuntimeException("wrong data format")*/
    }
}
