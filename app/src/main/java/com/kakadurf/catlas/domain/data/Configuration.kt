package com.kakadurf.catlas.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Configuration(
    @SerializedName("name")
    val name: String,
    @SerializedName("article")
    val article: String,
    @SerializedName("contextPath")
    val contextPath: String,
    @Transient
    var context: Array<ConfigurationContext>
)

@Parcelize
data class ConfigurationContext(
    @SerializedName("name")
    val name: String,
    @SerializedName("startDate")
    val startingDate: Long,
    @SerializedName("endDate")
    val endingDate: Long,
    @SerializedName("pictures")
    val pictures: Array<String>?,
    @SerializedName("description")
    val description: String
) : Parcelable

