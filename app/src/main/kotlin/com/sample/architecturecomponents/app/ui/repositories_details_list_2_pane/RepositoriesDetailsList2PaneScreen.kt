package com.sample.architecturecomponents.app.ui.repositories_details_list_2_pane

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.architecturecomponents.app.ui.AppState
import com.sample.architecturecomponents.app.ui.NavAppTopBar
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.ui.widgets.RoundedPlaceholderWidget
import com.sample.architecturecomponents.feature.repositories.RepositoriesScreen
import com.sample.architecturecomponents.feature.repositories.navigation.REPOSITORIES_ROUTE
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_DETAILS_ROUTE
import com.sample.architecturecomponents.feature.repository_details.navigation.navigateToRepositoryDetails
import com.sample.architecturecomponents.feature.repository_details.navigation.repositoryDetailsScreen
import timber.log.Timber
import com.sample.architecturecomponents.core.ui.R as CoreUiR

private const val REPOSITORIES_DETAILS_PANE_ROUTE = "repositories_details_pane_route"

fun NavGraphBuilder.repositoriesListDetailsScreen(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(route = REPOSITORIES_ROUTE) {
        RepositoriesListScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
internal fun RepositoriesListScreen(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: RepositoriesDetailsList2PaneViewModel = hiltViewModel(),
) {
    val selectedUser by viewModel.selectedRepository.collectAsStateWithLifecycle()
    RepositoriesListScreen(
        appState = appState,
        selectedRepoOwner = selectedUser?.userOwner,
        selectedRepoUrl = selectedUser?.userUrl,
        onRepositoryClick = viewModel::onRepositoryClick,
        onShowSnackbar = onShowSnackbar
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun RepositoriesListScreen(
    appState: AppState,
    selectedRepoOwner: String?,
    selectedRepoUrl: String?,
    onRepositoryClick: (String, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    val nestedNavController = rememberNavController()

    fun onRepoClickShowDetailPane(repoOwner: String, repoUrl: String) {
        Timber.d("onRepoClickShowDetailPane($repoOwner, $repoUrl)")
        onRepositoryClick(repoOwner, repoUrl)
        nestedNavController.navigateToRepositoryDetails(owner = repoOwner, repo = repoUrl) {
            popUpTo(REPOSITORIES_DETAILS_PANE_ROUTE)
        }
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
    }

    ListDetailPaneScaffold(
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            RepositoriesScreen(
                onRepositoryClick = ::onRepoClickShowDetailPane,
                onShowSnackbar = onShowSnackbar,
            )
        },
        detailPane = {
            Column(Modifier.fillMaxSize()) {
                Timber.d("detailPane() - AppState: ${appState.shouldShowBottomBar}")

                if (appState.shouldShowBottomBar) {
                    NavAppTopBar(
                        state = appState
                    )
                }

                NavHost(
                    navController = nestedNavController,
                    startDestination = REPOSITORY_DETAILS_ROUTE,
                    route = REPOSITORIES_DETAILS_PANE_ROUTE,
                ) {
                    composable(route = REPOSITORY_DETAILS_ROUTE) {
                        RoundedPlaceholderWidget(
                            header = CoreUiR.string.empty_placeholder_header,
                            image = Icons.Repositories,
                            imageContentDescription = CoreUiR.string.empty_placeholder_header,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                    repositoryDetailsScreen(
                        showBackButton = !listDetailNavigator.isListPaneVisible(),
                        onBackClick = listDetailNavigator::navigateBack,
                        onShowSnackbar = onShowSnackbar,
                        onUrlClick = {
                            Timber.d("detailPane() - repositoryDetailsScreen: $it")
                        },
                    )
                }
            }

        },
    )

    LaunchedEffect(Unit) {
        if (selectedRepoOwner != null && selectedRepoUrl != null) {
            onRepoClickShowDetailPane(selectedRepoOwner, selectedRepoUrl)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
