package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.datetime.Instant

@Entity(
    tableName = "RecentSearch",
    indices = [
        Index(value = ["value", "tag"], unique = true)
    ]
)
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "value") val value: String,
    @ColumnInfo(name = "date") val date: Instant,
    @ColumnInfo(name = "tag") val tag: RecentSearchTags,
)

fun RecentSearchEntity.asExternalModel() = RecentSearch(
    value = value,
    date = date,
    tag = tag,
)

fun List<RecentSearchEntity>.asExternalModel() = map { it.asExternalModel() }