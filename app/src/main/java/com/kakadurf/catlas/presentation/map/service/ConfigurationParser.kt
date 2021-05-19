package com.kakadurf.catlas.presentation.map.service

import android.content.Context
import com.google.gson.Gson
import com.kakadurf.catlas.domain.data.Configuration
import com.kakadurf.catlas.domain.data.ConfigurationContext

class ConfigurationParser(context: Context, configPath: String) {
    val configuration: Configuration = Gson().fromJson(
        context.assets.open(configPath).reader(),
        Configuration::class.java
    )
    private val configurationContext =
        Gson().fromJson(
            context.assets.open(configuration.contextPath).reader(),
            Array<ConfigurationContext>::class.java
        )

    init {
        configuration.context = configurationContext
    }
}
