package com.kakadurf.catlas.presentation.map.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.timeline.http.wiki.HistoricEvent
import com.kakadurf.catlas.presentation.general.view.ApplicationImpl
import com.kakadurf.catlas.presentation.map.maintaining.MapMaintainingService
import com.kakadurf.catlas.presentation.map.maintaining.MapMaintainingServiceImpl
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import kotlinx.android.synthetic.main.fr_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MapFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) : Fragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MapViewModel
    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.getMapComp()?.inject(this)
        this@MapFragment.activity?.let { it ->
            viewModel = ViewModelProvider(it, viewModelFactory).get(MapViewModel::class.java)
            launch(Dispatchers.IO) {
                viewModel.parseArticle("Timeline_of_historic_inventions")
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_map, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
        configureMap()
        viewModel.timeLineMap.observe(viewLifecycleOwner) { map ->
            // fix recreate issue
            onConfigDone(map)
        }
    }

    @ExperimentalCoroutinesApi
    @MainThread
    fun onConfigDone(timeLineMap: Map<Int, HistoricEvent>) {
        progressBar.visibility = View.GONE
        sb_year.max = timeLineMap.size - 1
        launch {
            callbackFlow<Pair<Int, JSONObject?>> {
                sb_year.setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            viewModel.getYearIndexed(progress)?.let {
                                curYear = it
                                offer(Pair(it, viewModel.getRegionJsonByYear(it)))
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    }
                )
                awaitClose {
                    sb_year?.setOnSeekBarChangeListener(null)
                }
            }.debounce(600L).collect {
                service?.clearMap()
                it.second?.let { it1 -> service?.addLayer(it1) }
                tv_year.text = "${it.first} year"
            }
        }
    }

    private var service: MapMaintainingService? = null
    private var curYear: Int? = null

    @MainThread
    private fun configureMap() {
        mapFragment?.getMapAsync { maps ->
            service = MapMaintainingServiceImpl(
                maps,
                MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
            service?.setOnClickListener {
                curYear?.let { year ->
                    val directions =
                        MapFragmentDirections.actionMainFragmentToExtendedInfoFragment(
                            viewModel.getInfoByYear(year)?.description
                        )
                    findNavController().navigate(directions)
                }
            }
        }
    }
}
