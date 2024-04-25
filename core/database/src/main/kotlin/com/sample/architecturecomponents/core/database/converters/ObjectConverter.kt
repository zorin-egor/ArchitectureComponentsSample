package com.sample.architecturecomponents.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal abstract class ObjectConverter<T>(
    private val typeToken: TypeToken<T>
) {
    @TypeConverter
    fun toJson(value: T): String = Gson().toJson(value, typeToken.type)

    @TypeConverter
    fun fromJson(json: String): T = Gson().fromJson(json, typeToken.type)
}