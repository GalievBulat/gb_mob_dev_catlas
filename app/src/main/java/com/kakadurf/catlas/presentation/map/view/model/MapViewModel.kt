package com.kakadurf.catlas.presentation.map.view.model

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.data.timeline.db.DBConfigDao
import com.kakadurf.catlas.data.timeline.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.config.Configuration
import com.kakadurf.catlas.domain.wiki.parser.HistoricEvent
import com.kakadurf.catlas.domain.wiki.parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki.parser.WikipediaParser
import com.kakadurf.catlas.presentation.map.service.ConfigurationParser
import com.kakadurf.catlas.presentation.map.service.LocalGeo
import com.kakadurf.catlas.presentation.map.service.MapInflater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.net.SocketTimeoutException
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
    lateinit var cacheDao: DBCacheDao

    @Inject
    lateinit var configDao: DBConfigDao

    private val mTimeLineMap: MutableLiveData<TreeMap<Int, HistoricEvent>> = MutableLiveData()
    private val mCurrentYear: MutableLiveData<Int> = MutableLiveData()
    private val mCurrentRegion: MutableLiveData<JSONObject> = MutableLiveData()
    private val mCurrentLocalConfiguration: MutableLiveData<Configuration> = MutableLiveData()
    private val mMapConfigurationCompletion: MutableLiveData<Boolean> = MutableLiveData()
    private val mConfigurationNames: MutableLiveData<List<String>> = MutableLiveData()

    val currentLocalConfiguration: LiveData<Configuration> = mCurrentLocalConfiguration
    val configurationNames: LiveData<List<String>> = mConfigurationNames
    val currentYear: LiveData<Int> = mCurrentYear
    val currentRegion: LiveData<JSONObject> = mCurrentRegion
    val timeLineMap: LiveData<TreeMap<Int, HistoricEvent>> = mTimeLineMap
    val mapConfigurationCompletion: LiveData<Boolean> = mMapConfigurationCompletion

    fun onMapConfigured() {
        mMapConfigurationCompletion.value = true
    }

    fun onMapDestroyed() {
        mMapConfigurationCompletion.value = false
    }

    private suspend fun parseArticle(article: String) {
        try {
            val rowText = wikiRepository.getAllWikiTextFromPage(article)
            val text = wikiTextCleanUp.cleanupWikiText(rowText)
            val timeLineMap = wikipediaParser.getTimelineMap(text)
            mapInflater.fillContours(timeLineMap)
            mTimeLineMap.postValue(timeLineMap)
        } catch (e: SocketTimeoutException) {
            Timber.e("timeout")
            parseArticle(article)
        }
    }

    @MainThread
    fun getYearIndexed(index: Int): Int? =
        mTimeLineMap.value?.run {
            keys.elementAt(index)
        }

    fun setYear(year: Int) {
        mCurrentYear.value = year
        viewModelScope.launch(Dispatchers.IO) {
            mTimeLineMap.value?.run {
                val region = get(year)?.region
                region?.let {
                    cacheDao.pullFromDB(region, year)?.json?.let {
                        mCurrentRegion.postValue(JSONObject(String(it)))
                    }
                }
            }
        }
    }

    fun getAllConfigNames() {
        viewModelScope.launch(Dispatchers.IO) {
            mConfigurationNames.postValue(
                configDao.getAllConfigNames()
            )
        }
    }

    fun getInfoByYear(year: Int): HistoricEvent? = mTimeLineMap.value?.get(year)

    fun setConfiguration(name: String = "Inventions TimeLine") {
        viewModelScope.launch(Dispatchers.IO) {
            val config = configDao.pullConfigFromDB(name)
            val contexts = configDao.getAllContextByGroup(config.contextGroup)
            config.contexts = contexts
            mCurrentLocalConfiguration.postValue(config)
            parseArticle(config.article)
        }
    }

    @Deprecated("in case of not using db")
    fun setLocalConfiguration(context: Context, path: String = "epochs.json") {
        viewModelScope.launch(Dispatchers.IO) {
            val configuration = (
                    ConfigurationParser(
                        context, "config_files/$path"
                    ).localConfiguration
                    )
            mCurrentLocalConfiguration.postValue(configuration)
            parseArticle(configuration.article)
        }
    }
}
