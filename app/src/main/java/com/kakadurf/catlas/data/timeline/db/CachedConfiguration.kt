package com.kakadurf.catlas.data.timeline.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kakadurf.catlas.domain.config.Configuration
import com.kakadurf.catlas.domain.config.TimelineContext

@Entity(
    tableName = "config",
    /*foreignKeys = [
        ForeignKey(
            entity = CachedContext::class,
            parentColumns = arrayOf("group"),
            childColumns = arrayOf("context_group")
        )
    ]*/
)
data class CachedConfiguration(

    @ColumnInfo(name = "name")
    @SerializedName("name")
    override val name: String,
    @ColumnInfo(name = "article")
    @SerializedName("article")
    override val article: String,
    @ColumnInfo(name = "context_group")
    @SerializedName("context_group")
    val contextGroup: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    ) : Configuration {
    @Ignore
    @Transient
    override var contexts: List<TimelineContext>? = null
}
