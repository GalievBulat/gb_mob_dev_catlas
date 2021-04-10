package com.kakadurf.catlas.data.http.openstreetmap

import android.util.Log
import org.json.JSONObject
import javax.inject.Inject


class RegionFetching @Inject constructor(val service: RegionHttpRetriever) {
    suspend fun getGeometries(name: String): JSONObject {
        Log.d("hi", "hi")
        return JSONObject(
            service.searchForARegion(name)
                .charStream().readText()
        )
    }
}