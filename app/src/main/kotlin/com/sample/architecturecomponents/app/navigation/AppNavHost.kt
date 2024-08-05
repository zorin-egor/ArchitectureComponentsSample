package com.sample.architecturecomponents.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sample.architecturecomponents.app.ui.AppState
import com.sample.architecturecomponents.app.ui.users_details_list_2_pane.usersListDetailsScreen
import com.sample.architecturecomponents.feature.repositories.navigation.repositoriesScreen
import com.sample.architecturecomponents.feature.repository_details.navigation.navigateToRepositoryDetails
import com.sample.architecturecomponents.feature.repository_details.navigation.repositoryDetailsScreen
import com.sample.architecturecomponents.feature.settings.navigation.settingsScreen
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
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
        usersListDetailsScreen(
            appState = appState,
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
