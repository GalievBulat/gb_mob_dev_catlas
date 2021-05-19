package com.kakadurf.catlas.presentation.map.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kakadurf.catlas.R
import com.kakadurf.catlas.domain.data.ConfigurationContext
import com.kakadurf.catlas.presentation.map.view.VpAdapter
import kotlinx.android.synthetic.main.fr_context.tv_context_description
import kotlinx.android.synthetic.main.fr_context.vp_context

class ContextFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_context, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val configurationContext = arguments?.getParcelable<ConfigurationContext?>("config")
        configurationContext?.let {
            val images = configurationContext.pictures
            images?.let {
                vp_context.adapter = VpAdapter(requireActivity(), images)
                tv_context_description.text = configurationContext.description
            }
        }
    }
}
