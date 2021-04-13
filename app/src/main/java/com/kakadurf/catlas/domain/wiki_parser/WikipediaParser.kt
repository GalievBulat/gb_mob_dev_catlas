package com.kakadurf.catlas.domain.wiki_parser

import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import java.util.*

class WikipediaParser(
    private val
    dateConverter: DateConverter,
    private val
    countryExtractor: CountryExtractor
) {
    fun parseTable(rowWikiTable: String, requestedHeader: String = "countries") {
        val table = rowWikiTable.dropWhile { it != '{' }.split("|-")
        val headers = table[0].split("!!")
        var desiredHeaderNumber = -1
        for (i in 0..headers.size) {
            if (headers[i].contains(requestedHeader, true)) {
                desiredHeaderNumber = i
            }
        }
        if (desiredHeaderNumber == -1)
            throw RuntimeException("no such header")
        for (i in 1..table.size) {
            table[i]
        }
        TODO()
        /*    for (header in headers.split("!")){
                if(header.equals(requestedHeader, ignoreCase = true)){

                }
            }*/
    }

    fun getTimelineMap(
        rowText: String,
        lineDelimiter: Char = '*',
        dateDelimiter: Char = ':'
    ): TreeMap<Int, HistoricEvent> {
        val lines = rowText.split(lineDelimiter)
        val resultingMap = TreeMap<Int, HistoricEvent>()
        for (i in 1 until lines.size) {
            val date = dateConverter.extractYear(lines[i].substringBefore(dateDelimiter))
            val rest = lines[i].substringAfter(dateDelimiter)
            val region = countryExtractor.extractRegion(rest)
            region?.let { reg ->
                date?.let { date ->
                    resultingMap[date] = HistoricEvent(reg, date, rest)
                }
            }
        }
        return resultingMap
    }

}