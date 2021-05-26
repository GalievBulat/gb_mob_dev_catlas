package com.kakadurf.catlas.presentation.general.di.component

import com.kakadurf.catlas.data.timeline.di.module.HttpRegionModule
import com.kakadurf.catlas.data.timeline.di.module.HttpWikiArticleFetchingModule
import com.kakadurf.catlas.data.timeline.di.module.HttpWikiImageFetchingModule
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import com.kakadurf.catlas.presentation.map.di.MapViewModelBindingModule
import com.kakadurf.catlas.presentation.map.di.MapViewModelModule
import com.kakadurf.catlas.presentation.map.di.WikiModule
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
