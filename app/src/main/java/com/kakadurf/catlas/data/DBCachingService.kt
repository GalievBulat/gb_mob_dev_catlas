package com.kakadurf.catlas.data

import com.kakadurf.catlas.data.db.CachedEntity
import com.kakadurf.catlas.data.db.DBCacheDao
import javax.inject.Inject

class DBCachingService @Inject constructor(private val dao: DBCacheDao?) : CachingService {
    override suspend fun saveToCache(region: String, json: String) {
        dao?.saveToDB(CachedEntity(0, region, json))
    }

    override suspend fun pullFromCache(region: String): String? = dao?.pullFromDB(region)?.json
}