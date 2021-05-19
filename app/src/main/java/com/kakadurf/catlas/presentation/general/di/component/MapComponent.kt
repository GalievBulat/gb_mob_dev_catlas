package com.kakadurf.catlas.presentation.general.di.component

import com.kakadurf.catlas.data.timeline.module.HttpRegionModule
import com.kakadurf.catlas.data.timeline.module.HttpWikiArticleFetchingModule
import com.kakadurf.catlas.data.timeline.module.HttpWikiImageFetchingModule
import com.kakadurf.catlas.domain.wiki.module.WikiModule
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import com.kakadurf.catlas.presentation.map.di.MapViewModelBindingModule
import com.kakadurf.catlas.presentation.map.di.MapViewModelModule
import com.kakadurf.catlas.presentation.map.view.fragment.MapFragment
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import dagger.Subcomponent

@MapScope
@Subcomponent(
    modules = [
        HttpWikiArticleFetchingModule::class, HttpWikiImageFetchingModule::class,
        HttpRegionModule::class,
        WikiModule::class, MapViewModelBindingModule::class,
        MapViewModelModule::class
    ]
)
interface MapComponent {
    fun inject(fragment: MapFragment)
    fun inject(mapViewModel: MapViewModel)
    @Subcomponent.Builder
    interface Builder {
        fun build(): MapComponent
    }
}
