package com.sample.architecturecomponents.core.database.converters

import androidx.room.TypeConverter
import com.sample.architecturecomponents.core.model.RecentSearchTags

internal class RecentSearchTagsConverter {
    @TypeConverter
    fun stringToEnum(value: String?): RecentSearchTags =
        value?.let(RecentSearchTags::valueOf)
            ?: RecentSearchTags.None

    @TypeConverter
    fun enumToString(value: RecentSearchTags?): String =
        value?.name ?: RecentSearchTags.None.name
}
