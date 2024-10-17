package com.sample.architecturecomponents.feature.users.models


sealed interface UsersActions {
    data object None : UsersActions
    data class ShowError(val error: Throwable) : UsersActions
    data class NavigateToDetails(val id: Long, val url: String) : UsersActions
}