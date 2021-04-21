package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakadurf.catlas.R


const val FRAGMENT_MANAGER_TAG = "FM_TAG"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
