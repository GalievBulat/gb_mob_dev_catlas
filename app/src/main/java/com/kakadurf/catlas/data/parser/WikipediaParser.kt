package com.kakadurf.catlas.data.parser

class WikipediaParser {
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
        dateConverter: DateConverter,
        countryExtracted: CountryExtracted,
        lineDelimiter: Char = '*',
        dateDelimiter: Char = ':'
    ):
            HashMap<Int, String> {
        val lines = rowText
            .split(lineDelimiter)
        val resultingMap = HashMap<Int, String>()
        for (i in 1..lines.size) {
            val date = dateConverter.extractYear(lines[i].substringBefore(dateDelimiter))
            val rest = lines[i].substringAfter(dateDelimiter)
            val region = countryExtracted.extractRegion(rest)
            resultingMap[date] = region
        }
        return resultingMap
    }

}