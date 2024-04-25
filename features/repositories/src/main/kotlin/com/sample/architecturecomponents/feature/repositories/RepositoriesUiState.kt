package com.sample.architecturecomponents.feature.repositories

import com.sample.architecturecomponents.core.model.Repository

internal data class RepositoriesByNameUiState(
    val query: String,
    val state: RepositoriesByNameUiStates
)

internal sealed interface RepositoriesByNameUiStates {
    data class Success(
        val repositories: List<Repository>,
        val isBottomProgress: Boolean
    ) : RepositoriesByNameUiStates
    data object Loading : RepositoriesByNameUiStates
    data object Empty : RepositoriesByNameUiStates
    data object Start: RepositoriesByNameUiStates
}