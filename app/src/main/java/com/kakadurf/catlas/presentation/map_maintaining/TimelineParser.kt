package com.kakadurf.catlas.presentation.map_maintaining

import com.kakadurf.catlas.data.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class TimelineParser @Inject constructor() {
    suspend fun fillContours(
        timeLineMap: Map<Int, HistoricEvent>,
        timelineContours: TreeMap<Int, JSONObject>,
        fetcher: RegionFetching
    ) {
        val regionToYearMap = HashMap<String, Int>()
        timelineContours.run {
            timeLineMap.entries.forEach {
                if (!regionToYearMap.containsKey(it.value.region)) {
                    this[it.key] = fetcher.getGeometries(it.value.region)
                    regionToYearMap[it.value.region] = it.key
                } else {
                    val year = regionToYearMap[it.value.region]
                    this[year]?.let { json ->
                        this[it.key] = json
                    }
                }
            }
        }
    }
}