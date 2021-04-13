package com.kakadurf.catlas.presentation.di.component

import com.kakadurf.catlas.data.di.module.CacheServiceModule
import com.kakadurf.catlas.data.di.module.DBModule
import com.kakadurf.catlas.presentation.view.MainFragment
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DBModule::class, CacheServiceModule::class])
interface DBComponent {
    fun inject(fragment: MainFragment)
}