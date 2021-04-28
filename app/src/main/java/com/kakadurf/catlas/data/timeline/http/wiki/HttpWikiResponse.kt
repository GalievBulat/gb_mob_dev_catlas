package com.kakadurf.catlas.data.timeline.http.wiki

import com.google.gson.annotations.SerializedName

data class HttpWikiResponse(
    @SerializedName("parse")
    var parse: Parse
)

data class Parse(
    @SerializedName("title")
    var title: String,
    @SerializedName("wikitext")
    var wikiText: WikiText
)

data class WikiText(
    @SerializedName("*")
    var text: String
)
