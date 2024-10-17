package com.sample.architecturecomponents.feature.user_details.models

sealed interface UserDetailsEvent {
    data object None : UserDetailsEvent
    data object ShareProfile : UserDetailsEvent
    data object NavigationBack : UserDetailsEvent
}