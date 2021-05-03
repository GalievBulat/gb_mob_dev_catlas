package com.kakadurf.catlas.presentation.general.di.component

import android.content.Context
import com.kakadurf.catlas.data.timeline.module.DBModule
import com.kakadurf.catlas.presentation.general.di.annotation.AppScope
import com.kakadurf.catlas.presentation.general.di.module.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        DBModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {
    fun plusMap(): MapComponent.Builder
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}
