package com.sample.architecturecomponents.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sample.architecturecomponents.app.ui.AppState
import com.sample.architecturecomponents.feature.details.navigation.detailsScreen
import com.sample.architecturecomponents.feature.details.navigation.navigateToDetails
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import com.sample.architecturecomponents.feature.users.navigation.usersScreen
import timber.log.Timber

@Composable
fun NavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = USERS_ROUTE,
) {
    Timber.d("NavHost($appState)")

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        usersScreen(
            showBackButton = true,
            onBackClick = navController::popBackStack,
            onUserClick = { id, url -> navController.navigateToDetails(userId = id, userUrl = url) },
            onShowSnackbar = onShowSnackbar
        )
        detailsScreen(
            showBackButton = true,
            onBackClick = navController::popBackStack,
            onUrlClick = {},
            onShowSnackbar = onShowSnackbar
        )
    }
}
