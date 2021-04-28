package com.kakadurf.catlas.presentation.general.di.component

import com.kakadurf.catlas.data.timeline.module.HttpRegionModule
import com.kakadurf.catlas.data.timeline.module.HttpWikiModule
import com.kakadurf.catlas.domain.wiki.module.WikiModule
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import com.kakadurf.catlas.presentation.map.di.ViewModelModule
import com.kakadurf.catlas.presentation.map.view.fragment.MapFragment
import dagger.Subcomponent

@MapScope
@Subcomponent(
    modules = [
        HttpWikiModule::class, HttpRegionModule::class,
        WikiModule::class, ViewModelModule::class
    ]
)
interface MapComponent {
    fun inject(fragment: MapFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): MapComponent
    }
}
