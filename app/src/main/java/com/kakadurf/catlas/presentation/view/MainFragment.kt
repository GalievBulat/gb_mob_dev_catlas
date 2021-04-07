package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.openstreetmap.RegionDataFetcher
import com.kakadurf.catlas.data.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki_parser.CountryExtractor
import com.kakadurf.catlas.domain.wiki_parser.DateConverter
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import kotlinx.android.synthetic.main.fr_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashSet
import kotlin.coroutines.CoroutineContext

class MainFragment(override val coroutineContext: CoroutineContext = Dispatchers.Main) : Fragment(),
    CoroutineScope {
    private lateinit var service: MapMaintainingServiceImpl
    private val timelineContours = TreeMap<Int, JSONObject>()
    private val fetcher = RegionDataFetcher()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch(Dispatchers.IO) {

            val rowText = WikiPageRepository()
                .getWikiTextSection(resources.getString(R.string.wikiPage), 3)

            val text = WikiTextCleanUp().cleanupWikiText(rowText)
            val timeLineMap = WikipediaParser()
                .getTimelineMap(
                    text,
                    DateConverter(),
                    CountryExtractor()
                )
            val countrySet = HashSet<String>()
            timeLineMap.entries.forEach {
                if (!countrySet.contains(it.value)) {
                    timelineContours[it.key] = fetcher.getGeometries(it.value)
                    countrySet.add(it.value)
                } else {
                    val l = timeLineMap.entries.find { entry ->
                        entry.value == it.value
                    }?.key
                    timelineContours[it.key] = timelineContours[l]!!
                }
            }
            //Log.d("hi", timelineContours.toString())
            this.launch MainFragment@{
                sb_year.max = timelineContours.size - 1
                sb_year.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        service.clearMap()
                        timelineContours.values.toList()[progress].let { service.addLayer(it) }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as SupportMapFragment
        mapFragment.getMapAsync { maps ->
            service = MapMaintainingServiceImpl(maps).apply {
                setOnClickListener {
                    if (it.hasProperty("display_name")) {
                        val name = it.getProperty("display_name")
                        tv_info.text = name
                    }
                }
            }
        }


    }
}