package com.sample.architecturecomponents.feature.users.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sample.architecturecomponents.feature.users.UsersScreen
import org.jetbrains.annotations.VisibleForTesting


@VisibleForTesting
internal const val SINCE_ID_ARG = "sinceId"
const val USERS_ROUTE = "users_route"

internal class UsersArgs(val sinceId: Long? = null) {
    constructor(savedStateHandle: SavedStateHandle) : this(savedStateHandle[SINCE_ID_ARG])
}

fun NavController.navigateToUsers(sinceId: Long? = null, navOptions: NavOptions) {
    val newRoute = USERS_ROUTE
    navigate(newRoute, navOptions)
}

fun NavGraphBuilder.usersScreen(
    onUserClick: (Long, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = USERS_ROUTE,
    ) {
        UsersScreen(onUserClick = onUserClick, onShowSnackbar = onShowSnackbar)
    }
}