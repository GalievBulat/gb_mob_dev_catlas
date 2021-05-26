package com.kakadurf.catlas.data.timeline.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ListConverter {
    @TypeConverter
    fun fromLangList(list: List<String>): ByteArray {
        val gson = Gson()
        return gson.toJson(list).toByteArray()
    }

    @TypeConverter
    fun toLangList(array: ByteArray): List<String> {
        var str = String(array)
        str = str.run {
            replace("\r", "")
                .replace("\n", "")
                .dropLastWhile { it != ']' }
        }
        return GsonBuilder().setLenient().create().fromJson(
            str, Array<String>::class.java
        ).toList()
    }
}
