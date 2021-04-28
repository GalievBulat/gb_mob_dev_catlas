package com.kakadurf.catlas.data.timeline.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DBCacheDao {
    @Insert
    suspend fun saveToDB(entity: CachedEntity)
    @Query("select * from cache where region =:region")
    suspend fun pullFromDB(region: String): CachedEntity?
}
