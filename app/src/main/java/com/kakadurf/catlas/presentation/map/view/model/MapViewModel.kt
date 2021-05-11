package com.kakadurf.catlas.presentation.map.view.model

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakadurf.catlas.data.timeline.db.CachedEntity
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.data.timeline.http.wiki.HistoricEvent
import com.kakadurf.catlas.data.timeline.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki.parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki.parser.WikipediaParser
import com.kakadurf.catlas.presentation.map.service.LocalGeo
import com.kakadurf.catlas.presentation.map.service.MapInflater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
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

    @Inject
    lateinit var localGeo: LocalGeo

    @Inject
    lateinit var dao: DBCacheDao

    private val mTimeLineMap: MutableLiveData<TreeMap<Int, HistoricEvent>> = MutableLiveData()
    private val mCurrentYear: MutableLiveData<Int> = MutableLiveData()
    private val mMapConfigCompletion: MutableLiveData<Boolean> = MutableLiveData()
    private val mCurrentRegion: MutableLiveData<JSONObject> = MutableLiveData()

    //val currentYear: LiveData<Int> = mCurrentYear
    val currentRegion: LiveData<JSONObject> = mCurrentRegion
    val timeLineMap: LiveData<TreeMap<Int, HistoricEvent>> = mTimeLineMap
    //val mapConfigCompletion: LiveData<Boolean> = mMapConfigCompletion


    private suspend fun parseArticle(article: String) {
        val rowText = wikiRepository.getAllWikiTextFromPage(article)
        val text = wikiTextCleanUp.cleanupWikiText(rowText)
        val timeLineMap = wikipediaParser.getTimelineMap(text)
        mapInflater.fillContours(timeLineMap)
        mTimeLineMap.postValue(timeLineMap)
    }

    @MainThread
    fun getYearIndexed(index: Int): Int? =
        mTimeLineMap.value?.run {
            keys.elementAt(index)
        }

    fun setYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mTimeLineMap.value?.run {
                val region = get(year)?.region
                region?.let {
                    dao.pullFromDB(region, year)?.json?.let {
                        mCurrentRegion.postValue(JSONObject(it.toString()))
                    }
                }
            }
        }
    }

    fun getInfoByYear(year: Int): HistoricEvent? = mTimeLineMap.value?.get(year)
    fun fetchDataForMap(article: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //getLocalRegions()
            parseArticle(article)
        }
    }

    private suspend fun getLocalRegions() {
        val timelineMap = localGeo.getAllLocalFeatures()
        if (dao.getDbSize() == 0)
            Timber.d("prepop map")
        timelineMap.forEach {
            dao.saveToDB(
                CachedEntity(
                    it.key,
                    it.value.second.toString().toByteArray(),
                    it.value.first
                )
            )
        }
    }
}
