package com.sample.architecturecomponents.feature.repositories.models

import com.sample.architecturecomponents.core.ui.viewmodels.UiState

data class RepositoriesUiState(
    val searchState: UiState<RecentSearchUiModel>,
    val repoState: UiState<RepositoriesUiModel>
)

val emptyRepositoriesUiState = RepositoriesUiState(
    searchState = UiState.Empty,
    repoState = UiState.Empty
)