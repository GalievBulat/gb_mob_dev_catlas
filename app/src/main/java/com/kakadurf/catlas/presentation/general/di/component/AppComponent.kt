package com.kakadurf.catlas.presentation.general.di.component

import android.content.Context
import com.kakadurf.catlas.data.timeline.module.DBModule
import com.kakadurf.catlas.presentation.general.di.annotation.DBScope
import dagger.BindsInstance
import dagger.Component

@DBScope
@Component(modules = [DBModule::class])
interface AppComponent {
    fun plusMap(): MapComponent.Builder
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}
