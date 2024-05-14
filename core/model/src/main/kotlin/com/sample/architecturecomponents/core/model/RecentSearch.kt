package com.sample.architecturecomponents.core.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class RecentSearch(
    val value: String,
    val date: Instant = Clock.System.now(),
    val tag: RecentSearchTags = RecentSearchTags.None,
)