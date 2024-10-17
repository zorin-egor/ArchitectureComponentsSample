package com.sample.architecturecomponents.feature.users.models

import com.sample.architecturecomponents.core.model.User

data class UsersUiModel(
    val users: List<User>,
    val isBottomProgress: Boolean
)
