package com.kakadurf.catlas.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakadurf.catlas.R

const val FRAGMENT_MANAGER_TAG = "FMTAG"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fr_main_activity_main, MainFragment(), FRAGMENT_MANAGER_TAG)
                .commit()
    }
}