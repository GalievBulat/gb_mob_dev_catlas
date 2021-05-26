package com.kakadurf.catlas.data.timeline.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DBConfigDao {
    @Insert
    suspend fun saveContextToDB(entity: CachedContext)

    @Insert
    suspend fun saveConfigToDB(entity: CachedConfiguration)

    @Query("select * from context where name =:name limit 1")
    suspend fun pullContextFromDB(name: String): CachedContext?

    @Query("select * from context")
    suspend fun getAllContext(): List<CachedContext>

    @Query("select * from context where id =:id  ")
    suspend fun getAllContextById(id: Long): List<CachedContext>

    @Query("select * from context where `group` =:group  ")
    suspend fun getAllContextByGroup(group: String): List<CachedContext>

    @Query("select * from config where name =:name ")
    suspend fun pullConfigFromDB(name: String): CachedConfiguration

    @Query("select name from config")
    suspend fun getAllConfigNames(): List<String>

    @Query("select distinct `group` from context")
    suspend fun getAllContextGroups(): List<String>
}
