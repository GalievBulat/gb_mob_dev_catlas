package com.kakadurf.catlas.presentation.map.di

import androidx.lifecycle.ViewModel
import com.kakadurf.catlas.presentation.general.di.annotation.MapScope
import com.kakadurf.catlas.presentation.general.di.annotation.ViewModelKey
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MapViewModelBindingModule {
    @MapScope
    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun bindMapViewModel(
        mapViewModel: MapViewModel
    ): ViewModel
}
