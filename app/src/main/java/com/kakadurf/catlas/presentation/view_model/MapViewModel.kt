package com.kakadurf.catlas.presentation.view_model

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import com.kakadurf.catlas.data.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingService
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import com.kakadurf.catlas.presentation.service.TimelineParser
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MapViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var wikiRepository: WikiPageRepository

    @Inject
    lateinit var wikiTextCleanUp: WikiTextCleanUp

    @Inject
    lateinit var wikipediaParser: WikipediaParser

    @Inject
    lateinit var timelineParser: TimelineParser

    val timeline: MutableLiveData<TreeMap<Int, HistoricEvent>> = MutableLiveData()
    val selectedThing: MutableLiveData<Int> = MutableLiveData()
    private lateinit var service: MapMaintainingService
    private val regionContours = HashMap<String, JSONObject>()

    @WorkerThread
    suspend fun parseArticle(article: String) {
        val rowText = wikiRepository
            .getAllWikiTextFromPage(article)
        val text = wikiTextCleanUp.cleanupWikiText(rowText)
        val timeLineMap = wikipediaParser.getTimelineMap(text)
        timeline.postValue(timeLineMap)
        timelineParser.fillContours(timeLineMap, regionContours)
    }

    @MainThread
    suspend fun onProgressChange(progress: Int) {
        service.clearMap()
        delay(600L)
        timeline.value?.run {
            keys.elementAt(progress).let {
                regionContours[get(it)?.region]?.let { json ->
                    service.addLayer(json)
                }
                selectedThing.value = it
            }
        }
    }

    fun setMap(context: Context, maps: GoogleMap, onClickListener: (Int) -> Unit) {
        service = MapMaintainingServiceImpl(maps, context).apply {
            setOnClickListener {
                selectedThing.value?.let {
                    onClickListener(it)
                }
            }
        }
    }

    fun getInfoByYear(year: Int): HistoricEvent? = timeline.value?.get(year)

}

