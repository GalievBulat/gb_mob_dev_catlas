package com.kakadurf.catlas.presentation.map.service

import android.content.Context
import com.google.gson.Gson
import com.kakadurf.catlas.domain.config.LocalConfiguration
import com.kakadurf.catlas.domain.config.LocalConfigurationContext

class ConfigurationParser(context: Context, configPath: String) {
    val localConfiguration: LocalConfiguration = Gson().fromJson(
        context.assets.open(configPath).reader(),
        LocalConfiguration::class.java
    )
    private val configurationContext =
        Gson().fromJson(
            context.assets.open(localConfiguration.contextPath).reader(),
            Array<LocalConfigurationContext>::class.java
        )

    init {
        localConfiguration.contexts = configurationContext.toList()
    }
}
