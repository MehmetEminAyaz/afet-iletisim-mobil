package com.example.bitirmev2.data

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.split(",")
    }
}
