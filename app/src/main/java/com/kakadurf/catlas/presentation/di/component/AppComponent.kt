package com.kakadurf.catlas.presentation.di.component

import android.content.Context
import com.kakadurf.catlas.data.di.module.DBModule
import com.kakadurf.catlas.data.di.module.HttpRegionModule
import com.kakadurf.catlas.data.di.module.HttpWikiModule
import com.kakadurf.catlas.domain.module.WikiModule
import dagger.BindsInstance
import dagger.Component

///!!!!!
@Component(
    modules = [HttpWikiModule::class, HttpRegionModule::class,
        WikiModule::class]
)
interface AppComponent {
    fun plusDB(module: DBModule): DBComponent

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}