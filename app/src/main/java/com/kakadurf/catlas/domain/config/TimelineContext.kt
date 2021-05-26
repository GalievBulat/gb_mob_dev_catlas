package com.kakadurf.catlas.domain.config

import android.os.Parcelable

interface TimelineContext : Parcelable {
    val name: String
    val startingDate: Long
    val endingDate: Long
    val description: String
    val pictures: List<String>?
}
