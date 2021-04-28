package com.kakadurf.catlas.presentation.map.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakadurf.catlas.presentation.general.di.annotation.ViewModelKey
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import com.kakadurf.catlas.presentation.map.view.model.MapViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: MapViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun bindMapViewModel(
        mapViewModel: MapViewModel
    ): ViewModel
}
