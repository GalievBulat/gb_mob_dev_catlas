package com.kakadurf.catlas.data.timeline.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache")
data class CachedEntity(
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "geo_json") val json: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "year") val year: Int? = null
)
