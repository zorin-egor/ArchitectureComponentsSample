package com.sample.architecturecomponents.feature.repositories

import com.sample.architecturecomponents.core.designsystem.component.SearchTextDataItem
import com.sample.architecturecomponents.core.model.Repository

data class RepositoriesByNameUiState(
    val query: String,
    val recentSearch: List<SearchTextDataItem>,
    val state: RepositoriesByNameUiStates
)

sealed interface RepositoriesByNameUiStates {
    data class Success(
        val repositories: List<Repository>,
        val isBottomProgress: Boolean
    ) : RepositoriesByNameUiStates
    data object Loading : RepositoriesByNameUiStates
    data object Empty : RepositoriesByNameUiStates
    data object Start: RepositoriesByNameUiStates
}