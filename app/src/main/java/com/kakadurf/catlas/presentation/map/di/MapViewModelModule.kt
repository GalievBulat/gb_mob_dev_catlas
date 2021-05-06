package com.kakadurf.catlas.presentation.map.di

import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import dagger.Module
import dagger.Provides

@Module
class MapViewModelModule {
    @MapScope
    @Provides
    fun getMapViewModule() = MapViewModel()


}
