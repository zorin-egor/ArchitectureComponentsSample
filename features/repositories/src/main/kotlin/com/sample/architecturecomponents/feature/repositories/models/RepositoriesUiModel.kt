package com.sample.architecturecomponents.feature.repositories.models

import com.sample.architecturecomponents.core.designsystem.component.SearchTextDataItem
import com.sample.architecturecomponents.core.model.Repository

data class RepositoriesUiModel(
    val repositories: List<Repository>,
    val isBottomProgress: Boolean
)

data class RecentSearchUiModel(
    val query: String,
    val recentSearch: List<SearchTextDataItem>,
)