package com.sample.architecturecomponents.feature.details

import com.sample.architecturecomponents.core.model.Details

sealed interface DetailsUiState {
    data class Success(val details: Details) : DetailsUiState
    data object Loading : DetailsUiState
}