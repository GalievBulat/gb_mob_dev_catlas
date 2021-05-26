package com.kakadurf.catlas.presentation.general.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kakadurf.catlas.R
import com.kakadurf.catlas.presentation.general.view.model.ConfigurationCreationViewModel
import kotlinx.android.synthetic.main.fr_new_context.bt_config_save
import kotlinx.android.synthetic.main.fr_new_context.et_config_article
import kotlinx.android.synthetic.main.fr_new_context.et_config_name
import kotlinx.android.synthetic.main.fr_new_context.spinner_config_contexts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ConfigurationCreationFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
) :
    Fragment(), CoroutineScope {
    lateinit var viewModel: ConfigurationCreationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ConfigurationCreationViewModel::class.java)
        ApplicationImpl.appComponent?.inject(viewModel)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_new_context, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { activity ->
            var curContext: String? = null
            viewModel.getConfigGroups()
            viewModel.contextNames.observe(viewLifecycleOwner) { groups ->
                val adapter = ArrayAdapter(
                    activity,
                    android.R.layout.simple_spinner_item,
                    groups.toTypedArray()
                )
                spinner_config_contexts.adapter = adapter
                spinner_config_contexts.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            curContext = groups[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                bt_config_save.setOnClickListener {
                    viewModel.saveConfig(
                        et_config_article.text.toString(),
                        et_config_name.text.toString(),
                        curContext ?: ""
                    )
                }
            }
        }
    }
}
