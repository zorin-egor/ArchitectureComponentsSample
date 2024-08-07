package com.sample.architecturecomponents.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sample.architecturecomponents.app.ui.AppState
import com.sample.architecturecomponents.feature.repositories.navigation.repositoriesScreen
import com.sample.architecturecomponents.feature.repository_details.navigation.navigateToRepositoryDetails
import com.sample.architecturecomponents.feature.repository_details.navigation.repositoryDetailsScreen
import com.sample.architecturecomponents.feature.settings.navigation.settingsScreen
import com.sample.architecturecomponents.feature.user_details.navigation.navigateToUserDetails
import com.sample.architecturecomponents.feature.user_details.navigation.userDetailsScreen
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import com.sample.architecturecomponents.feature.users.navigation.usersScreen
import timber.log.Timber

@Composable
fun NavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    showThemeDialog: () -> Unit,
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
            onUserClick = { id, url ->
                navController.navigateToUserDetails(userId = id, userUrl = url)
            },
            onShowSnackbar = onShowSnackbar
        )
        userDetailsScreen(
            showBackButton = true,
            onBackClick = navController::navigateUp,
            onUrlClick = {
                Timber.d("userDetailsScreen($it)")
            },
            onShowSnackbar = onShowSnackbar
        )
        settingsScreen(
            showThemeDialog = showThemeDialog,
            onShowSnackbar = onShowSnackbar
        )
        repositoriesScreen(
            onRepositoryClick = { owner, repo ->
                Timber.d("repositoriesScreen($owner, $repo)")
                navController.navigateToRepositoryDetails(owner, repo)
            },
            onShowSnackbar = onShowSnackbar
        )
        repositoryDetailsScreen(
            showBackButton = true,
            onBackClick = navController::navigateUp,
            onUrlClick = {
                Timber.d("repositoryDetailsScreen($it)")
            },
            onShowSnackbar = onShowSnackbar
        )
    }
}
