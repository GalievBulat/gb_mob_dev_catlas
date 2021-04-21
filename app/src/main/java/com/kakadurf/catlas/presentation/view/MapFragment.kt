package com.kakadurf.catlas.presentation.view

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
import com.kakadurf.catlas.R
import com.kakadurf.catlas.data.http.wiki.HistoricEvent
import com.kakadurf.catlas.presentation.view_model.MapViewModel
import kotlinx.android.synthetic.main.fr_map.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MapFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) : Fragment(), CoroutineScope {

    private var mapFragment: SupportMapFragment? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationImpl.dbComponent.inject(this)
        this@MapFragment.activity?.let { it ->
            viewModel = ViewModelProvider(it, viewModelFactory).get(MapViewModel::class.java)
            launch(Dispatchers.IO) {
                viewModel.parseArticle("Timeline_of_historic_inventions")
                print(viewModel.timeline.value.toString())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.iv_map) as? SupportMapFragment
        configureMap()
        viewModel.timeline.observe(viewLifecycleOwner) { map ->
            onConfigDone(map)
        }
        viewModel.selectedThing.observe(viewLifecycleOwner) {
            tv_year.text = "$it year"
        }

    }

    @MainThread
    fun onConfigDone(timeLineMap: Map<Int, HistoricEvent>) {
        progressBar.visibility = View.GONE
        sb_year.max = timeLineMap.size - 1
        sb_year.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                launch {
                    viewModel.onProgressChange(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    @MainThread
    private fun configureMap() {
        mapFragment?.getMapAsync { maps ->
            this@MapFragment.activity?.let {
                viewModel.setMap(it, maps) { year ->
                    val directions = MapFragmentDirections
                        .actionMainFragmentToExtendedInfoFragment(
                            viewModel.getInfoByYear(year)?.description
                        )
                    findNavController().navigate(directions)
                }
            }
        }
    }
}