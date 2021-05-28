package com.kakadurf.catlas.domain.wiki.parser

import java.util.TreeMap

class WikipediaParser(
    private val
    dateConverter: DateConverter,
    private val
    countryExtractor: CountryExtractor
) {
    @Deprecated("not yet implemented")
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
        /*    for (header in headers.split("!")){
                if(header.equals(requestedHeader, ignoreCase = true)){

                }
            }*/
    }

    fun getTimelineMap(
        rowText: String,
        lineDelimiter: String = "*",
        articleDelimiter: String = "==",
        dateDelimiter: String = ":"
    ): TreeMap<Int, HistoricEvent> {
        val lines = rowText.split(lineDelimiter)
        val resultingMap = TreeMap<Int, HistoricEvent>()
        for (i in 1 until lines.size) {
            val line = lines[i].substringBefore(articleDelimiter)
            val date = dateConverter.extractYear(line.substringBefore(dateDelimiter))
            val rest = line.substringAfter(dateDelimiter)
            val region = countryExtractor.extractRegion(rest)
            region?.let { reg ->
                date?.let { date ->
                    resultingMap[date] = HistoricEvent(reg.trim(), date, convertToText(rest))
                }
            }
        }
        return resultingMap
    }

    private fun convertToText(
        wikiText: String,
        linkStart: String = "[[",
        linkEnd: String = "]]",
        referenceRegex: String = "<?ref>.*?<?ref>"
    ) =
        wikiText.replace(linkStart, "")
            .replace(linkEnd, "")
            .replace("|", " ")
            .replace("#", " ")
            .replace(Regex(referenceRegex, RegexOption.DOT_MATCHES_ALL), "")
}
