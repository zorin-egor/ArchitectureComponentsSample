package com.sample.architecturecomponents.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import com.sample.architecturecomponents.app.ui.AppState
import com.sample.architecturecomponents.app.ui.repositories_details_list_2_pane.repositoriesListDetailsScreen
import com.sample.architecturecomponents.app.ui.users_details_list_2_pane.usersListDetailsScreen
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
fun AppNavHost(
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
        usersDetailsPaneScreen(
            appState = appState,
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )

        settingsScreen(
            showThemeDialog = showThemeDialog,
            onShowSnackbar = onShowSnackbar
        )

        repositoriesDetailsPaneScreen(
            appState = appState,
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}

private fun NavGraphBuilder.usersDetailsPaneScreen(
    appState: AppState,
    navController: NavController,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    if (appState.shouldShowNavRail) {
        usersListDetailsScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
    } else {
        usersScreen(
            onUserClick = { id, url ->
                navController.navigateToUserDetails(userId = id, userUrl = url)
            },
            onShowSnackbar = onShowSnackbar
        )
        userDetailsScreen(
            isTopBarVisible = true,
            onShowSnackbar = onShowSnackbar
        )
    }
}

private fun NavGraphBuilder.repositoriesDetailsPaneScreen(
    appState: AppState,
    navController: NavController,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    if (appState.shouldShowNavRail) {
        repositoriesListDetailsScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
    } else {
        repositoriesScreen(
            onRepositoryClick = { owner, repo ->
                navController.navigateToRepositoryDetails(owner = owner, repo = repo)
            },
            onShowSnackbar = onShowSnackbar
        )
        repositoryDetailsScreen(
            isTopBarVisible = true,
            onBackClick = navController::navigateUp,
            onUrlClick = {
                Timber.d("userDetailsScreen($it)")
            },
            onShowSnackbar = onShowSnackbar
        )
    }
}
