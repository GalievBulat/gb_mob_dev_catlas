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
    private val cachingService: DBCacheDao,
    private val localGeo: LocalGeo
) {
    suspend fun fillContours(
        timeLineMap: Map<Int, HistoricEvent>,
        regionContours: HashMap<String, JSONObject>
    ) {
        timeLineMap.values.forEach {
            if (!regionContours.containsKey(it.region)) {
                regionContours[it.region] =
                    (cachingService.pullFromDB(it.region)?.let { cache ->
                        Log.d("hi", "cache")
                        JSONObject(cache.json)
                    } ?: JSONObject(
                        fetcher.getGeometries(it.region).also { json ->
                            cachingService.saveToDB(CachedEntity(it.region, json))
                        }
                    ))
            }

        }
    }
}
