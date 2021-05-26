package com.kakadurf.catlas.data.timeline.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kakadurf.catlas.domain.config.TimelineContext
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "context")
@Parcelize

data class CachedContext(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    override val name: String,

    @ColumnInfo(name = "start")
    @SerializedName("startDate")
    override val startingDate: Long,

    @ColumnInfo(name = "end")
    @SerializedName("endDate")
    override val endingDate: Long,

    @ColumnInfo(name = "group")
    @SerializedName("group")
    val group: String,

    @ColumnInfo(
        typeAffinity = ColumnInfo.BLOB,
        name = "pictures"
    )
    override val pictures: List<String>,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    override val description: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    ) : TimelineContext
