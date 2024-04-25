package com.sample.architecturecomponents.feature.user_details

import com.sample.architecturecomponents.core.model.Details

sealed interface UserDetailsUiState {
    data class Success(val details: Details) : UserDetailsUiState
    data object Loading : UserDetailsUiState

}