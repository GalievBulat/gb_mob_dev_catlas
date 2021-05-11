package com.kakadurf.catlas.presentation.map.service

import com.kakadurf.catlas.data.timeline.db.CachedEntity
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.data.timeline.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.data.timeline.http.wiki.HistoricEvent
import javax.inject.Inject

class MapInflater @Inject constructor(
    private val fetcher: RegionFetching,
    private val cachingService: DBCacheDao
) {
    suspend fun fillContours(
        timeLineMap: Map<Int, HistoricEvent>
    ) {
        timeLineMap.values.forEach {
            if (cachingService.countSpecific(it.region) == 0) {
                println("${it.region} is null")
                fetcher.getGeometries(it.region).also { json ->
                    cachingService.saveToDB(CachedEntity(it.region, json.toByteArray()))
                }
            }
        }
    }
}
