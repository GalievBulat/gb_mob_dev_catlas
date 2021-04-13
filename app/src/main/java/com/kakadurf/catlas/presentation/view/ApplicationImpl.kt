package com.kakadurf.catlas.presentation.view

import android.app.Application
import com.kakadurf.catlas.data.di.module.DBModule
import com.kakadurf.catlas.presentation.di.component.DBComponent
import com.kakadurf.catlas.presentation.di.component.DaggerAppComponent

class ApplicationImpl : Application() {
    companion object {
        lateinit var dbComponent: DBComponent
    }

    override fun onCreate() {
        dbComponent = DaggerAppComponent.builder().context(applicationContext)
            .build()
            .plusDB(DBModule())
        super.onCreate()

    }
}