package com.kakadurf.catlas.presentation.map.service

import com.kakadurf.catlas.data.timeline.db.CachedEntity
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.data.timeline.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.domain.wiki.parser.HistoricEvent
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
                fetcher.getGeometries(it.region).also { json ->
                    cachingService.saveToDB(CachedEntity(it.region, json.toByteArray()))
                }
            }
        }
    }
}
