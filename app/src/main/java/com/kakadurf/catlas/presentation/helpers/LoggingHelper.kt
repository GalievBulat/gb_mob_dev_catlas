package com.kakadurf.catlas.presentation.helpers

import android.util.Log

const val TAG = "hi"

class LoggingHelper {
    companion object {
        fun sout(msg: String) {
            Log.d(TAG, msg)
        }
    }

}