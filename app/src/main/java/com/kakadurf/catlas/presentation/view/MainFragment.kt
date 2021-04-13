package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import com.kakadurf.catlas.data.http.wiki.WikiPageRepository
import com.kakadurf.catlas.domain.wiki_parser.WikiTextCleanUp
import com.kakadurf.catlas.domain.wiki_parser.WikipediaParser
import com.kakadurf.catlas.presentation.helper.TimelineParser
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingService
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import kotlinx.android.synthetic.main.fr_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext

class MainFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) :
    Fragment(), CoroutineScope {

    @Inject
    lateinit var wikiRepository: WikiPageRepository

    @Inject
    lateinit var wikiTextCleanUp: WikiTextCleanUp

    @Inject
    lateinit var wikipediaParser: WikipediaParser

    @Inject
    lateinit var timelineParser: TimelineParser


    private lateinit var service: MapMaintainingService

    private val regionContours = HashMap<String, JSONObject>()
    private var timeLineMap: TreeMap<Int, HistoricEvent> = TreeMap()
    private var selected: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.dbComponent.inject(this)
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
            timeLineMap = wikipediaParser.getTimelineMap(text)
            timelineParser.fillContours(timeLineMap, regionContours)
            launch(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                sb_year.max = timeLineMap.size - 1
                sb_year.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        service.clearMap()
                        //TODO(refactor)
                        var year: Int? = null
                        var i = 0
                        for (entry in timeLineMap) {
                            if (progress == i) {
                                year = entry.key
                                break
                            }
                            i++
                        }
                        //
                        regionContours[timeLineMap[year]?.region]?.let {
                            service.addLayer(it)
                        }
                        selected = year
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
        mapFragment?.getMapAsync { maps ->
            this@MainFragment.activity?.let {
                service = MapMaintainingServiceImpl(maps, it).apply {
                    setOnClickListener {
                        selected?.let { year ->
                            //TODO(refactor)
                            tv_year.text = year.toString()
                            tv_info.text = timeLineMap[year]?.description
                        }
                    }
                }
            }
        }


    }
}