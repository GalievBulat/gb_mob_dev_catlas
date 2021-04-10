package com.kakadurf.catlas.presentation.di.component

import com.kakadurf.catlas.data.di.module.HttpRegionModule
import com.kakadurf.catlas.data.di.module.HttpWikiModule
import dagger.Component

@Component(modules = [HttpWikiModule::class, HttpRegionModule::class])
interface HttpComponent {
    //fun injectHttp(fragment: MainFragment)
    fun plusWikiParser(): WikiParsingComponent.Builder
}