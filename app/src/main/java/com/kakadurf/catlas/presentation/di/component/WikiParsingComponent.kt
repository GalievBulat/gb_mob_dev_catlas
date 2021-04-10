package com.kakadurf.catlas.presentation.di.component

import com.kakadurf.catlas.domain.module.WikiModule
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import com.kakadurf.catlas.presentation.view.MainFragment
import dagger.Subcomponent

@Subcomponent(modules = [WikiModule::class])
interface WikiParsingComponent {
    fun inject(fragment: MainFragment)
    fun parser(): WikipediaParser
    fun cleanup(): WikiTextCleanUp

    @Subcomponent.Builder
    interface Builder {
        fun wikiModule(wiki: WikiModule): Builder
        fun build(): WikiParsingComponent
    }
}