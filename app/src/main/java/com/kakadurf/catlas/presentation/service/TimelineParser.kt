package com.kakadurf.catlas.presentation.service

import android.util.Log
import com.kakadurf.catlas.data.CachingService
import com.kakadurf.catlas.data.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import org.json.JSONObject
import javax.inject.Inject

//TODO(wtf)
class TimelineParser @Inject constructor(
    private val fetcher: RegionFetching,
    private val cachingService: CachingService
) {
    suspend fun fillContours(
        timeLineMap: Map<Int, HistoricEvent>,
        regionContours: HashMap<String, JSONObject>
    ) {
        timeLineMap.values.forEach {
            it.region.let { region ->
                if (!regionContours.containsKey(region)) {
                    regionContours[region] =
                        cachingService.pullFromCache(region)?.let { cache ->
                            Log.d("hi", "cache")
                            JSONObject(cache)
                        } ?: JSONObject(fetcher.getGeometries(region).also { json ->
                            cachingService.saveToCache(region, json)
                        })
                }
            }
        }
    }
}