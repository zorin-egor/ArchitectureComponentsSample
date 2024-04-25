package com.sample.architecturecomponents.feature.users

import com.sample.architecturecomponents.core.model.User

sealed interface UsersUiState {
    data class Success(
        val users: List<User>,
        val isBottomProgress: Boolean
    ) : UsersUiState
    data object Loading : UsersUiState
    data object Empty : UsersUiState
}