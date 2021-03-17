package com.kakadurf.catlas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.fr_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fr_main)
        iv_map.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                Log.d("hi", event.x.toString() + " " + event.y)
            }
            v.performClick()
            return@setOnTouchListener true
        }
    }
}