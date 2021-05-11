package com.kakadurf.catlas.data.timeline.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DBCacheDao {
    @Insert
    suspend fun saveToDB(entity: CachedEntity)

    @Query("select * from cache where region =:region limit 1;")
    suspend fun pullFromDB(region: String): CachedEntity?

    @Query("select COUNT(id) from cache;")
    suspend fun getDbSize(): Int

    @Query("select * from cache where region =:region and (year = :year or year is null) limit 1;")
    suspend fun pullFromDB(region: String, year: Int): CachedEntity?

    @Query("select COUNT(id) from cache where region =:region limit 1")
    suspend fun countSpecific(region: String): Int
}
