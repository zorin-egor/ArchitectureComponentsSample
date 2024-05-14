package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.RecentSearchEntity
import com.sample.architecturecomponents.core.model.RecentSearch

internal fun RecentSearch.toRecentSearchEntity() = RecentSearchEntity(
    value = value,
    date = date,
    tag = tag,
)