package com.kakadurf.catlas.presentation.helper

import android.util.Log

class LoggingHelper {
    companion object {
        fun sout(msg: String) {
            Log.d(TAG, msg)
        }
    }

}