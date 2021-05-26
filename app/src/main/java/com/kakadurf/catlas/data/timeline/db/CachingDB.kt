package com.kakadurf.catlas.data.timeline.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DB_NAME = "cahcingDb"

@TypeConverters(ListConverter::class)
@Database(
    entities = [CachedEntity::class, CachedConfiguration::class, CachedContext::class],
    version = 1
)
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
                    .createFromAsset("db.db")
                    .build()
            }
            return instance
        }
    }

    abstract fun getCacheDao(): DBCacheDao

    abstract fun getConfigDao(): DBConfigDao
}
