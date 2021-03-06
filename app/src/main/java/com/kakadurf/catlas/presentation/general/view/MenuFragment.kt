package com.kakadurf.catlas.presentation.general.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kakadurf.catlas.R
import kotlinx.android.synthetic.main.fr_menu.bt_menu_create_config
import kotlinx.android.synthetic.main.fr_menu.bt_menu_info
import kotlinx.android.synthetic.main.fr_menu.bt_menu_map

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fr_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(findNavController()) {
            bt_menu_map.setOnClickListener {
                navigate(R.id.action_menuFragment_to_mainFragment)
            }
            bt_menu_create_config.setOnClickListener {
                navigate(R.id.action_menuFragment_to_configurationCreationFragment)
            }
            bt_menu_info.setOnClickListener {
                navigate(R.id.action_menuFragment_to_helpFragment)
            }
        }
    }
}
