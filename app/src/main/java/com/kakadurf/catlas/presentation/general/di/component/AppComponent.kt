package com.kakadurf.catlas.presentation.general.di.component

import android.content.Context
import com.kakadurf.catlas.data.timeline.di.module.DBModule
import com.kakadurf.catlas.presentation.general.di.annotation.AppScope
import com.kakadurf.catlas.presentation.general.di.module.ViewModelFactoryModule
import com.kakadurf.catlas.presentation.general.view.model.ConfigurationCreationViewModel
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
    fun inject(configurationCreationViewModel: ConfigurationCreationViewModel)
    fun plusMap(): MapComponent.Builder
    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }
}
