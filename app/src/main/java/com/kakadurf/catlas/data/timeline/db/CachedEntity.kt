package com.kakadurf.catlas.data.timeline.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache")
data class CachedEntity(
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "geo_json")
    val json: ByteArray,
    @ColumnInfo(name = "year") val year: Int? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
