package com.kakadurf.catlas.domain.config

interface Configuration {
    val name: String
    val article: String
    val contexts: List<TimelineContext>?
}
