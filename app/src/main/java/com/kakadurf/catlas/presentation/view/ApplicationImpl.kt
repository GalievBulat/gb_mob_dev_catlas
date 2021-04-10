package com.kakadurf.catlas.presentation.view

import android.app.Application
import com.kakadurf.catlas.data.di.module.HttpRegionModule
import com.kakadurf.catlas.data.di.module.HttpWikiModule
import com.kakadurf.catlas.domain.module.WikiModule
import com.kakadurf.catlas.presentation.di.component.DaggerHttpComponent
import com.kakadurf.catlas.presentation.di.component.WikiParsingComponent

class ApplicationImpl : Application() {
    companion object {
        lateinit var httpComponent: WikiParsingComponent
    }

    override fun onCreate() {
        httpComponent = DaggerHttpComponent.builder()
            .httpRegionModule(HttpRegionModule())
            .httpWikiModule(HttpWikiModule()).build().plusWikiParser().wikiModule(WikiModule())
            .build()
        super.onCreate()
    }
}