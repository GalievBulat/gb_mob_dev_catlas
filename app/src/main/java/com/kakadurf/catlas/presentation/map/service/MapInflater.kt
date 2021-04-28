package com.kakadurf.catlas.presentation.map.service

import android.util.Log
import com.kakadurf.catlas.data.timeline.db.CachedEntity
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.data.timeline.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.data.timeline.http.wiki.HistoricEvent
import org.json.JSONObject
import javax.inject.Inject

class MapInflater @Inject constructor(
    private val fetcher: RegionFetching,
    private val cachingService: DBCacheDao
) {
    suspend fun fillContours(
        timeLineMap: Map<Int, HistoricEvent>,
        regionContours: HashMap<String, JSONObject>
    ) {
        timeLineMap.values.forEach {
            it.region.let { region ->
                if (!regionContours.containsKey(region)) {
                    regionContours[region] =
                        (
                                cachingService.pullFromDB(region)?.let { cache ->
                                    Log.d("hi", "cache")
                                    JSONObject(cache.json)
                                } ?: JSONObject(
                                    fetcher.getGeometries(region).also { json ->
                                        cachingService.saveToDB(CachedEntity(0, region, json))
                                    }
                                )
                                )
                }
            }
        }
    }
}
