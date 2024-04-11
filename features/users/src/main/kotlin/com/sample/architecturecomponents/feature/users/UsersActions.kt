package com.sample.architecturecomponents.feature.users


sealed interface UsersActions {
    data class ShowError(val error: String) : UsersActions
}