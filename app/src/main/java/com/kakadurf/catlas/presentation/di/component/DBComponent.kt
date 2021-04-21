package com.kakadurf.catlas.presentation.di.component

import com.kakadurf.catlas.data.di.module.CacheServiceModule
import com.kakadurf.catlas.data.di.module.DBModule
import com.kakadurf.catlas.presentation.view.MapFragment
import com.kakadurf.catlas.presentation.view_model.ViewModelModule
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DBModule::class, CacheServiceModule::class, ViewModelModule::class])
interface DBComponent {
    fun inject(fragment: MapFragment)
}