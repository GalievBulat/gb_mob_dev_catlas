package com.kakadurf.catlas.presentation.map.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.kakadurf.catlas.R
import com.kakadurf.catlas.domain.wiki.parser.HistoricEvent
import com.kakadurf.catlas.presentation.general.view.ApplicationImpl
import com.kakadurf.catlas.presentation.map.maintaining.MapMaintainingService
import com.kakadurf.catlas.presentation.map.maintaining.MapMaintainingServiceImpl
import com.kakadurf.catlas.presentation.map.service.setListener
import com.kakadurf.catlas.presentation.map.view.SpinnerOnSelectedListenerExtention
import com.kakadurf.catlas.presentation.map.view.managing.AnimationManaging
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import kotlinx.android.synthetic.main.fr_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MapFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) : Fragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MapViewModel

    @Inject
    lateinit var animationManaging: AnimationManaging

    @Inject
    lateinit var spinnerOnSelectedListenerExtension: SpinnerOnSelectedListenerExtention

    private lateinit var mapService: MapMaintainingService
    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.getMapComp()?.inject(this)
        super.onCreate(savedInstanceState)
        this@MapFragment.activity?.let { it ->
            viewModel = ViewModelProvider(it, viewModelFactory)
                .get(MapViewModel::class.java).also { map ->
                    ApplicationImpl.getMapComp()?.inject(map)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_map, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createMapService()
        startTheShow()
    }

    override fun onResume() {
        super.onResume()
        viewModel.timeLineMap.observe(viewLifecycleOwner) { map ->
            onTimelineReady(map)
        }
    }

    override fun onDestroy() {
        viewModel.onMapDestroyed()
        ApplicationImpl.invalidateMapComponent()
        super.onDestroy()
    }

    @InternalCoroutinesApi
    private fun startTheShow() {
        animationManaging
            .startLoading(
                tv_map_loading1, tv_map_loading1_2,
                tv_map_loading2, tv_map_loading2_2, iv_map_loading_backgound
            )
        setUpSpinner()
    }

    @MainThread
    private fun onTimelineReady(timeLineMap: Map<Int, HistoricEvent>) {
        viewModel.currentYear.observe(viewLifecycleOwner) {
            tv_map_year.text = "$it year"
        }
        animationManaging.stopLoading(
            tv_map_loading1, tv_map_loading1_2,
            tv_map_loading2, tv_map_loading2_2, iv_map_loading_backgound
        )
        viewModel.mapConfigurationCompletion.observe(viewLifecycleOwner) {
            if (it) {
                inflateMapWithTimeline(timeLineMap, mapService)
            }
        }
        bt_map_ctxt.setOnClickListener {
            val year = viewModel.currentYear.value
            year?.let {
                val directions =
                    MapFragmentDirections.actionMainFragmentToContextFragment2(
                        viewModel.currentLocalConfiguration.value?.contexts?.find {
                            it.startingDate <= year && it.endingDate > year
                        }
                    )
                findNavController().navigate(directions)
            }
        }
    }

    @MainThread
    private fun createMapService() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fr_map) as? SupportMapFragment
        mapFragment?.getMapAsync { maps ->
            mapService = MapMaintainingServiceImpl(
                maps,
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )
            mapService.setOnClickListener {
                viewModel.currentYear.value?.let { year ->
                    val directions =
                        MapFragmentDirections.actionMainFragmentToExtendedInfoFragment(
                            viewModel.getInfoByYear(year)?.description
                        )
                    findNavController().navigate(directions)
                }
            }
            viewModel.onMapConfigured()
        }
    }

    private fun setUpSpinner() {
        activity?.let { activity ->
            viewModel.getAllConfigNames()
            viewModel.configurationNames.observe(viewLifecycleOwner) { configs ->
                val adapter = ArrayAdapter(
                    activity,
                    android.R.layout.simple_spinner_item,
                    configs
                )
                spinner_map_configuration_chooser.adapter = adapter
                viewModel.currentLocalConfiguration.observe(viewLifecycleOwner) { config ->
                    var index = configs.indexOf(config.name)
                    if (index == -1)
                        index = 0
                    spinner_map_configuration_chooser.setSelection(index)
                }
                spinnerOnSelectedListenerExtension.run {
                    spinner_map_configuration_chooser.setSelectListener { position ->
                        Timber.d(configs[position])
                        if (configs[position] != viewModel.currentLocalConfiguration.value?.name) {
                            viewModel.setConfiguration(configs[position])
                            animationManaging.startLoading(
                                tv_map_loading1, tv_map_loading1_2,
                                tv_map_loading2, tv_map_loading2_2, iv_map_loading_backgound
                            )
                        }
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun inflateMapWithTimeline(
        timeLineMap: Map<Int, HistoricEvent>,
        service: MapMaintainingService
    ) {
        viewModel.currentRegion.observe(viewLifecycleOwner) {
            service.addLayer(it)
        }
        seekbar_map_year.max = timeLineMap.size - 1
        launch(Dispatchers.Main) {
            callbackFlow<Int> {
                seekbar_map_year.setListener { progress ->
                    viewModel.getYearIndexed(progress)?.let {
                        offer(it)
                    }
                }
                awaitClose {
                    seekbar_map_year?.setOnSeekBarChangeListener(null)
                }
            }
                .debounce(600L)
                .collect {
                    service.clearMap()
                    viewModel.setYear(it)
                }
        }
    }
}
