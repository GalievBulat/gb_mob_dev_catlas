package com.kakadurf.catlas.domain.config

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LocalConfiguration(

    @SerializedName("name")
    override val name: String,

    @SerializedName("article")
    override val article: String,
    @SerializedName("contextPath")
    val contextPath: String,
    @Transient
    override var contexts: List<LocalConfigurationContext>
) : Configuration

@Parcelize
data class LocalConfigurationContext(

    @SerializedName("name")
    override val name: String,
    @SerializedName("startDate")
    override val startingDate: Long,
    @SerializedName("endDate")
    override val endingDate: Long,
    @SerializedName("pictures")
    override val pictures: List<String>?,
    @SerializedName("description")
    override val description: String
) : Parcelable, TimelineContext
