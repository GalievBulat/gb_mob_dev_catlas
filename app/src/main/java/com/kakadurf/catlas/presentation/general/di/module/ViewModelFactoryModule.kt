package com.kakadurf.catlas.presentation.general.di.module

import androidx.lifecycle.ViewModelProvider
import com.kakadurf.catlas.presentation.map.view.model.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {
    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory
}
