package com.sample.architecturecomponents.feature.repository_details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sample.architecturecomponents.feature.repository_details.RepositoryDetailsScreen
import timber.log.Timber

const val REPOSITORY_ID = "repo_id"
const val REPOSITORY_OWNER = "repo_owner"
const val REPOSITORY_DETAILS_ROUTE = "repository_details_route"
const val REPOSITORY_DETAILS_ROUTE_PATH = "$REPOSITORY_DETAILS_ROUTE?$REPOSITORY_OWNER={$REPOSITORY_OWNER}&$REPOSITORY_ID={$REPOSITORY_ID}"

internal class RepositoryDetailsArgs(val owner: String, val repo: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle[REPOSITORY_OWNER]), checkNotNull(savedStateHandle[REPOSITORY_ID]))
}

fun NavController.navigateToRepositoryDetails(owner: String, repo: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    val newRoute = "$REPOSITORY_DETAILS_ROUTE?$REPOSITORY_OWNER=$owner&$REPOSITORY_ID=$repo"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.repositoryDetailsScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = REPOSITORY_DETAILS_ROUTE_PATH,
        arguments = listOf(
            navArgument(REPOSITORY_OWNER) { type = NavType.StringType },
            navArgument(REPOSITORY_ID) { type = NavType.StringType },
        ),
    ) {
        Timber.d("DetailsScreen() - detailsScreen")
        RepositoryDetailsScreen(
            onUrlClick = onUrlClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}