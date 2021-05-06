package com.kakadurf.catlas.data.timeline.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kakadurf.catlas.presentation.map.service.LocalGeo
import org.json.JSONObject

const val DB_NAME = "cahcingDb"

@Database(entities = [CachedEntity::class], version = 2)
abstract class CachingDB : RoomDatabase() {
    companion object {
        private lateinit var instance: CachingDB
        var timeLineMap: Map<String, Pair<Int, JSONObject>>? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                timeLineMap?.forEach {
                    database.insert(
                        "cache",
                        SQLiteDatabase.CONFLICT_REPLACE,
                        ContentValues().apply {
                            put("region", it.key)
                            put("year", it.value.first)
                            put("geo_json", it.value.second.toString())
                        }
                    )
                }
                timeLineMap = null
            }
        }

        @Synchronized
        fun create(context: Context, localGeo: LocalGeo): CachingDB {
            if (!::instance.isInitialized) {
                timeLineMap = localGeo.getAllLocalFeatures(context)
                instance = Room.databaseBuilder(
                    context,
                    CachingDB::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return instance
        }
    }
    abstract fun getDao(): DBCacheDao
}
