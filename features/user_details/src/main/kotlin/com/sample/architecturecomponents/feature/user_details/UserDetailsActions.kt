package com.sample.architecturecomponents.feature.user_details


sealed interface UserDetailsActions {
    data class ShowError(val error: String) : UserDetailsActions
}