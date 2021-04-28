package com.kakadurf.catlas.domain.wiki.module

import com.kakadurf.catlas.domain.wiki.parser.CountryExtractor
import com.kakadurf.catlas.domain.wiki.parser.DateConverter
import com.kakadurf.catlas.domain.wiki.parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki.parser.WikipediaParser
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import dagger.Module
import dagger.Provides

@Module
class WikiModule {
    @Provides
    @MapScope
    fun provideCleanup(): WikiTextCleanUp = WikiTextCleanUp()

    @Provides
    @MapScope
    fun provideParser(
        dateConverter: DateConverter,
        countryExtractor: CountryExtractor
    ) =
        WikipediaParser(dateConverter, countryExtractor)
}
