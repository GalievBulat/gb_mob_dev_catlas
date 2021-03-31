package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.presentation.map_maintaining.MapMaintainingServiceImpl
import kotlinx.android.synthetic.main.fr_main.*

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
}