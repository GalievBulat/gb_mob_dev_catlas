package com.kakadurf.catlas.presentation.map_maintaining

import org.json.JSONObject

interface MapMaintainingService {
    fun clearMap()
    fun addLayer(json: JSONObject)
}