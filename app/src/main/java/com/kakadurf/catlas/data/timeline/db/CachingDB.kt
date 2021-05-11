package com.kakadurf.catlas.data.timeline.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DB_NAME = "cahcingDb"

@Database(entities = [CachedEntity::class], version = 1)
abstract class CachingDB : RoomDatabase() {
    companion object {
        private lateinit var instance: CachingDB

        @Synchronized
        fun create(context: Context): CachingDB {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    CachingDB::class.java,
                    DB_NAME
                )
                    .build()
            }
            return instance
        }
    }
    abstract fun getDao(): DBCacheDao
}
