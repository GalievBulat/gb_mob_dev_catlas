package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.WikiTextFetcher
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import kotlinx.android.synthetic.main.fr_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            WikiTextFetcher().getWikiTextSection("Timeline_of_historic_inventions", 3)
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as SupportMapFragment
        activity?.let { activity ->
            mapFragment.getMapAsync { maps ->
                MapMaintainingServiceImpl().apply {
                    setOnClickListener {
                        if (it.hasProperty("name")) {
                            val name = it.getProperty("name")
                            tv_info.text = name
                        }
                    }
                }.provideMap(maps, activity)
            }
        }
        /*val timeLineMap = WikipediaParser()
                .getTimelineMap(WikiTextCleanUp().cleanupWikiText(""),
                        DateConverter(),
                        CountryExtracted())*/
        sb_year.max = 260
        sb_year.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //tv_info.text = timeLineMap[320-progress]
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })

    }
}