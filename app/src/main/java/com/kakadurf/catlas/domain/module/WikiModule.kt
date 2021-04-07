package com.kakadurf.catlas.domain.module

import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import dagger.Module
import dagger.Provides

@Module
class WikiModule {
    @Provides
    fun provideCleanup() = WikiTextCleanUp()

    @Provides
    fun provideParser() = WikipediaParser()
}