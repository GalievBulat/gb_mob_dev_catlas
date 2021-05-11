package com.kakadurf.catlas.presentation.map.view.fragment

import android.annotation.SuppressLint
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
    private var curYear: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.getMapComp()?.inject(this)
        super.onCreate(savedInstanceState)
        this@MapFragment.activity?.let { it ->
            viewModel = ViewModelProvider(it, viewModelFactory)
                .get(MapViewModel::class.java).also { map ->
                    ApplicationImpl.getMapComp()?.inject(map)
                }
            viewModel.fetchDataForMap("Timeline_of_historic_inventions")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        /*val binding: FrMapBinding =
            DataBindingUtil.setContentView(requireActivity(), R.layout.fr_map)
        return binding.root*/
        return inflater.inflate(R.layout.fr_map, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val adapter = ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, arrayOf("Historic Inventions")
            )
            spinner.adapter = adapter
            mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
            configureMap()
            viewModel.timeLineMap.observe(viewLifecycleOwner) { map ->
                // fix recreate issue
                onConfigDone(map)
            }
        }
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
                    tv_year.text = "$it year"
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
