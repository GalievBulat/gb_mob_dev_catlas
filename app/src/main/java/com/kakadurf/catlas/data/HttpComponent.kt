package com.kakadurf.catlas.data

import androidx.fragment.app.Fragment
import com.kakadurf.catlas.data.http.wiki.HttpWikiModule
import dagger.Component

@Component(modules = [HttpWikiModule::class])
interface HttpComponent {
    fun injectHttp(fragment: Fragment)
}