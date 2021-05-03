package com.kakadurf.catlas.presentation.map.view.model

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakadurf.catlas.data.timeline.http.wiki.HistoricEvent
import com.kakadurf.catlas.data.timeline.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki.parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki.parser.WikipediaParser
import com.kakadurf.catlas.presentation.map.service.MapInflater
import org.json.JSONObject
import java.util.TreeMap
import javax.inject.Inject

class MapViewModel : ViewModel() {
    @Inject
    lateinit var wikiRepository: WikiPageRepository

    @Inject
    lateinit var wikiTextCleanUp: WikiTextCleanUp

    @Inject
    lateinit var wikipediaParser: WikipediaParser

    @Inject
    lateinit var mapInflater: MapInflater

    private val mTimeLineMap: MutableLiveData<TreeMap<Int, HistoricEvent>> = MutableLiveData()
    private val mCurrentYear: MutableLiveData<Int> = MutableLiveData()
    private val mMapConfigCompletion: MutableLiveData<Boolean> = MutableLiveData()

    val currentYear: LiveData<Int> = mCurrentYear
    val timeLineMap: LiveData<TreeMap<Int, HistoricEvent>> = mTimeLineMap
    val mapConfigCompletion: LiveData<Boolean> = mMapConfigCompletion

    private val regionContours = HashMap<String, JSONObject>()

    @WorkerThread
    suspend fun parseArticle(article: String) {
        val rowText = wikiRepository
            .getAllWikiTextFromPage(article)
        val text = wikiTextCleanUp.cleanupWikiText(rowText)
        val timeLineMap = wikipediaParser.getTimelineMap(text)
        mapInflater.fillContours(timeLineMap, regionContours)
        mTimeLineMap.postValue(timeLineMap)
    }

    @MainThread
    fun getYearIndexed(index: Int): Int? =
        mTimeLineMap.value?.run {
            keys.elementAt(index)
        }

    fun getRegionJsonByYear(year: Int): JSONObject? =
        mTimeLineMap.value?.run {
            regionContours[get(year)?.region]
        }

    fun getInfoByYear(year: Int): HistoricEvent? = mTimeLineMap.value?.get(year)
}
