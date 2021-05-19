package com.kakadurf.catlas.domain.data

data class TimeContext(
    val description: String,
    val Years: IntRange,
)

val timeEpochs = mapOf(
    -2500000..-3000 to "The Stone Age",
    -3000..-1300 to "The Bronze Age",
    -1300..600 to "The Iron Age"
)
