package com.kakadurf.catlas.presentation.map.maintaining

import com.google.maps.android.data.Feature
import org.json.JSONObject

interface MapMaintainingService {
    fun clearMap()
    fun addLayer(json: JSONObject)
    fun setOnClickListener(onClickListener: (Feature) -> Unit)
}
