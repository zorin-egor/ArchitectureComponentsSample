package com.sample.architecturecomponents.feature.user_details

import com.sample.architecturecomponents.core.model.UserDetails

sealed interface UserDetailsUiState {
    data class Success(val userDetails: UserDetails) : UserDetailsUiState
    data object Loading : UserDetailsUiState
}