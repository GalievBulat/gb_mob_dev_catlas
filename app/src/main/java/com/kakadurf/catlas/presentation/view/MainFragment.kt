package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.openstreetmap.RegionFetching
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import com.kakadurf.catlas.data.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki_parser.CountryExtractor
import com.kakadurf.catlas.domain.wiki_parser.DateConverter
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import com.kakadurf.catlas.presentation.map_maintaining.TimelineParser
import kotlinx.android.synthetic.main.fr_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) :
    Fragment(), CoroutineScope {

    @Inject
    lateinit var fetcher: RegionFetching

    @Inject
    lateinit var wikiRepository: WikiPageRepository

    @Inject
    lateinit var wikiTextCleanUp: WikiTextCleanUp

    @Inject
    lateinit var wikipediaParser: WikipediaParser

    @Inject
    lateinit var timelineParser: TimelineParser

    @Inject
    lateinit var dateConverter: DateConverter

    @Inject
    lateinit var countryExtractor: CountryExtractor

    private lateinit var service: MapMaintainingServiceImpl
    private val timelineContours = TreeMap<Int, JSONObject>()
    private var timeLineMap: Map<Int, HistoricEvent> = TreeMap()
    private var selected: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.httpComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

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
            val rowText = wikiRepository
                .getAllWikiTextFromPage(resources.getString(R.string.wikiPage))
            val text = wikiTextCleanUp.cleanupWikiText(rowText)
            timeLineMap = wikipediaParser
                .getTimelineMap(
                    text,
                    dateConverter,
                    countryExtractor
                )
            Log.d("hi", timeLineMap.toString())
            timelineParser.fillContours(timeLineMap, timelineContours, fetcher)
            launch(Dispatchers.Main) {
                sb_year.max = timelineContours.size - 1
                sb_year.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        service.clearMap()
                        timelineContours.apply {
                            values.toList()[progress].let {
                                service.addLayer(it)
                            }
                            selected = progress
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
        mapFragment?.getMapAsync { maps ->
            service = MapMaintainingServiceImpl(maps).apply {
                setOnClickListener {
                    tv_info.text = timeLineMap.values.toList()[selected!!].description
                }
            }
        }


    }
}