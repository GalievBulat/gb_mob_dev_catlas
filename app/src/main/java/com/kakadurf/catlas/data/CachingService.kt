package com.kakadurf.catlas.data

interface CachingService {
    suspend fun saveToCache(region: String, json: String)
    suspend fun pullFromCache(region: String): String?
}