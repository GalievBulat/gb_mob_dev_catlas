package com.kakadurf.catlas.domain.module

import com.kakadurf.catlas.domain.wiki_parser.CountryExtractor
import com.kakadurf.catlas.domain.wiki_parser.DateConverter
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import dagger.Module
import dagger.Provides

@Module
class WikiModule {
    @Provides
    fun provideCleanup(): WikiTextCleanUp = WikiTextCleanUp()


    @Provides
    fun provideParser(
        dateConverter: DateConverter,
        countryExtractor: CountryExtractor
    ) =
        WikipediaParser(dateConverter, countryExtractor)
}