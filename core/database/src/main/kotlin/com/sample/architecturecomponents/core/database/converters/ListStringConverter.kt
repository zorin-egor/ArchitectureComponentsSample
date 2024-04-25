package com.sample.architecturecomponents.core.database.converters

import androidx.room.TypeConverter

internal class ListStringConverter {
    @TypeConverter
    fun stringToList(value: String?): List<String>? =
        value?.split("::")

    @TypeConverter
    fun listToString(value: List<String>?): String? =
        value?.joinToString(separator = "::") { it }
}
