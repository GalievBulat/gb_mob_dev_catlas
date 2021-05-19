package com.kakadurf.catlas.presentation.map.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.kakadurf.catlas.presentation.map.service.setListener
import com.kakadurf.catlas.presentation.map.view.model.MapViewModel
import kotlinx.android.synthetic.main.fr_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
    @Inject
    lateinit var viewModel: MapViewModel
    private var mapFragment: SupportMapFragment? = null
    private var service: MapMaintainingService? = null

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
        mapFragment =
            childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
        setUpSpinner()
        configureMap()
        viewModel.timeLineMap.observe(viewLifecycleOwner) { map ->
            onConfigDone(map)
        }
    }

    override fun onDestroy() {
        ApplicationImpl.invalidateMap()
        super.onDestroy()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @SuppressLint("SetTextI18n")
    @MainThread
    fun onConfigDone(timeLineMap: Map<Int, HistoricEvent>) {
        viewModel.currentRegion.observe(viewLifecycleOwner) {
            service?.addLayer(it)
        }
        viewModel.currentYear.observe(viewLifecycleOwner) {
            tv_year.text = "$it year"
        }
        bt_ctxt.setOnClickListener {
            val year = viewModel.currentYear.value
            year?.let {
                val directions = MapFragmentDirections
                    .actionMainFragmentToContextFragment2(
                        viewModel.currentConfiguration.value?.context?.find {
                            it.startingDate <= year && it.endingDate > year
                        }
                    )
                findNavController().navigate(directions)
            }
        }
        progressBar.visibility = View.GONE
        sb_year.max = timeLineMap.size - 1
        launch(Dispatchers.Main) {
            callbackFlow<Int> {
                sb_year.setListener { progress ->
                    viewModel.getYearIndexed(progress)?.let {
                        offer(it)
                    }
                }
                awaitClose {
                    sb_year?.setOnSeekBarChangeListener(null)
                }
            }
                .debounce(600L)
                .collect {
                    service?.clearMap()
                    viewModel.setYear(it)
                }
        }
    }

    private fun setUpSpinner() {
        activity?.let { activity ->
            val configs = (activity.assets
                .list("config_files") ?: arrayOf<String>())
                .filter { it.endsWith(".json") }
                .toTypedArray()
            val adapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_item,
                configs
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    Timber.d(configs.get(position)?.toString())
                    viewModel.setConfiguration(activity, configs[position].toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    @MainThread
    private fun configureMap() {
        mapFragment?.getMapAsync { maps ->
            service = MapMaintainingServiceImpl(
                maps,
                MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
            service?.setOnClickListener {
                viewModel.currentYear.value?.let { year ->
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
