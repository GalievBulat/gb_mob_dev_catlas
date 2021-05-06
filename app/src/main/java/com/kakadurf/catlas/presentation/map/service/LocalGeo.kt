package com.kakadurf.catlas.presentation.map.service

import android.content.Context
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonParser
import org.json.JSONObject
import javax.inject.Inject

class LocalGeo @Inject constructor() {
    val fileNamesList: Map<Int, String> = mapOf(
        -2000 to "world_bc2000.geojson",
        -1000 to "world_bc1000.geojson",
        -500 to "world_bc500.geojson",
        -323 to "world_bc323.geojson",
        -200 to "world_bc200.geojson",
        -1 to "world_bc1.geojson",
        400 to "world_400.geojson",
        600 to "world_600.geojson",
        800 to "world_800.geojson",
        1000 to "world_1000.geojson",
        1279 to "world_1279.geojson",
        1492 to "world_1492.geojson",
        1530 to "world_1530.geojson",
        1650 to "world_1650.geojson",
        1715 to "world_1715.geojson",
        1783 to "world_1783.geojson",
        1815 to "world_1815.geojson",
        1880 to "world_1880.geojson",
        1914 to "world_1914.geojson",
        1920 to "world_1920.geojson",
        1938 to "world_1938.geojson",
        1945 to "world_1945.geojson",
        1994 to "world_1994.geojson"
    )

    fun getFeature(
        jsonObject: JSONObject,
        region: String
    ) =
        GeoJsonParser(jsonObject).features
            .find {
                it.getProperty("NAME") == region
                        ||
                        it.getProperty("ABBREVNAME") == region
            }

    fun getFeaturesRegion(feature: GeoJsonFeature): String? =
        feature.getProperty("NAME") ?: feature.getProperty("ABBREVNAME")

    fun getFittestFile(year: Int) =
        when (year) {
            in -1500..-750 -> "world_bc1000.geojson"
            in -750..-400 -> "world_bc500.geojson"
            in -400..-250 -> "world_bc323.geojson"
            in -250..-100 -> "world_bc200.geojson"
            in -100..200 -> "world_bc1.geojson"
            in 200..500 -> "world_400.geojson"
            in 500..700 -> "world_600.geojson"
            in 700..900 -> "world_800.geojson"
            in 900..1100 -> "world_1000.geojson"
            in 1100..1400 -> "world_1279.geojson"
            in 1400..1500 -> "world_1492.geojson"
            in 1500..1600 -> "world_1530.geojson"
            in 1600..1700 -> "world_1650.geojson"
            in 1700..1750 -> "world_1715.geojson"
            in 1750..1800 -> "world_1783.geojson"
            in 1800..1850 -> "world_1815.geojson"
            in 1850..1900 -> "world_1880.geojson"
            in 1900..1918 -> "world_1914.geojson"
            in 1918..1930 -> "world_1920.geojson"
            in 1930..1945 -> "world_1938.geojson"
            in 1945..1980 -> "world_1945.geojson"
            in 1980..2100 -> "world_1994.geojson"
            else -> "world_bc2000.geojson"
        }

    //TODO(SUSPEND!!!)
    fun getAllLocalFeatures(context: Context): Map<String, Pair<Int, JSONObject>> =
        HashMap<String, Pair<Int, JSONObject>>().also { map ->
            fileNamesList.forEach {
                context.assets
                    .open("historicalmaps/" + it.value).reader().use { reader ->
                        val json = JSONObject(
                            reader.readText()
                        )
                        GeoJsonParser(
                            json
                        ).features.forEach { feature ->
                            getFeaturesRegion(feature)?.let { region ->
                                map[region] = (it.key to json)
                            }
                        }
                    }
            }
        }


}