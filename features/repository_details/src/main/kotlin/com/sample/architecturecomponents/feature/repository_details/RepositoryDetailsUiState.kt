package com.sample.architecturecomponents.feature.repository_details

import com.sample.architecturecomponents.core.model.RepositoryDetails

sealed interface RepositoryDetailsUiState {
    data class Success(val repositoryDetails: RepositoryDetails) : RepositoryDetailsUiState
    data object Loading : RepositoryDetailsUiState
}