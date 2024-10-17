package com.sample.architecturecomponents.feature.users.models

import com.sample.architecturecomponents.core.model.User


sealed interface UsersEvents {
    data object None : UsersEvents
    data class OnUserClick(val item: User): UsersEvents
    data object NextUser : UsersEvents
}