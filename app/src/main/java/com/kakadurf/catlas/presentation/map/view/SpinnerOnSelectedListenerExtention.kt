package com.kakadurf.catlas.presentation.map.view

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import javax.inject.Inject

class SpinnerOnSelectedListenerExtention @Inject constructor() {
    fun Spinner.setSelectListener(function: (Int) -> Unit) {
        onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    function(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }
}