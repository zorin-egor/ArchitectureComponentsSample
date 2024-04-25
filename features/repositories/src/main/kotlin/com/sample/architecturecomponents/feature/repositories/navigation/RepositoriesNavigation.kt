package com.sample.architecturecomponents.feature.repositories.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sample.architecturecomponents.feature.repositories.RepositoriesScreen

const val REPOSITORIES_ROUTE = "repositories_route"

fun NavController.navigateToRepositories(navOptions: NavOptions) {
    val newRoute = REPOSITORIES_ROUTE
    navigate(newRoute, navOptions)
}

fun NavGraphBuilder.repositoriesScreen(
    onRepositoryClick: (Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = REPOSITORIES_ROUTE,
    ) {
        RepositoriesScreen(onRepositoryClick = onRepositoryClick, onShowSnackbar = onShowSnackbar )
    }
}