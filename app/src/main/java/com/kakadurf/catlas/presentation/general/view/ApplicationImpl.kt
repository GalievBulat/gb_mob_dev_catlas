package com.kakadurf.catlas.presentation.general.view

import android.app.Application
import com.kakadurf.catlas.presentation.general.di.component.AppComponent
import com.kakadurf.catlas.presentation.general.di.component.DaggerAppComponent
import com.kakadurf.catlas.presentation.general.di.component.MapComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class ApplicationImpl :
    Application() {
    companion object {
        var appComponent: AppComponent? = null
        private var mapComponent: MapComponent? = null

        @Synchronized
        fun getMapComp(): MapComponent? =
            if (mapComponent == null) appComponent?.plusMap()?.build() else mapComponent

        fun invalidateMapComponent() {
            mapComponent = null
        }

        fun invalidateApp() {
            appComponent = null
        }
    }

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()
        Timber.plant(DebugTree())
        super.onCreate()
    }
}
