package com.sample.architecturecomponents.app.ui.users_details_list_2_pane

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
import com.sample.architecturecomponents.core.ui.R
import com.sample.architecturecomponents.core.ui.widgets.RoundedPlaceholderWidget
import com.sample.architecturecomponents.feature.user_details.navigation.USER_DETAILS_ROUTE
import com.sample.architecturecomponents.feature.user_details.navigation.navigateToUserDetails
import com.sample.architecturecomponents.feature.user_details.navigation.userDetailsScreen
import com.sample.architecturecomponents.feature.users.UsersScreen
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import timber.log.Timber

private const val USER_DETAILS_PANE_ROUTE = "user_details_pane_route"

fun NavGraphBuilder.usersListDetailsScreen(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(route = USERS_ROUTE) {
        UsersListScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
internal fun UsersListScreen(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: UsersDetailsList2PaneViewModel = hiltViewModel(),
) {
    val selectedUser by viewModel.selectedUser.collectAsStateWithLifecycle()
    UsersListScreen(
        appState = appState,
        selectedUserId = selectedUser?.userId,
        selectedUserUrl = selectedUser?.userUrl,
        onUserClick = viewModel::onUserClick,
        onShowSnackbar = onShowSnackbar
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun UsersListScreen(
    appState: AppState,
    selectedUserId: Long?,
    selectedUserUrl: String?,
    onUserClick: (Long, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    val nestedNavController = rememberNavController()

    fun onUserClickShowDetailPane(userId: Long, userUrl: String) {
        Timber.d("onUserClickShowDetailPane($userId, $userUrl)")
        onUserClick(userId, userUrl)
        nestedNavController.navigateToUserDetails(userId = userId, userUrl = userUrl) {
            popUpTo(USER_DETAILS_PANE_ROUTE)
        }
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
    }

    ListDetailPaneScaffold(
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            UsersScreen(
                onUserClick = ::onUserClickShowDetailPane,
                onShowSnackbar = onShowSnackbar,
            )
        },
        detailPane = {
            Column(Modifier.fillMaxSize()) {
                Timber.d("detailPane() - AppState: ${appState.shouldShowBottomBar}")

                if (appState.shouldShowBottomBar) {
                    NavAppTopBar(
                        state = appState,
                    )
                }

                NavHost(
                    navController = nestedNavController,
                    startDestination = USER_DETAILS_ROUTE,
                    route = USER_DETAILS_PANE_ROUTE,
                ) {
                    composable(route = USER_DETAILS_ROUTE) {
                        RoundedPlaceholderWidget(
                            header = R.string.empty_placeholder_header,
                            image = Icons.Users,
                            imageContentDescription = R.string.empty_placeholder_header,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                    userDetailsScreen(
                        isTopBarVisible = !listDetailNavigator.isListPaneVisible(),
                        onBackClick = listDetailNavigator::navigateBack,
                        onShowSnackbar = onShowSnackbar,
                        onUrlClick = {
                            Timber.d("detailPane() - userDetailsScreen: $it")
                        },
                    )
                }
            }

        },
    )

    LaunchedEffect(Unit) {
        if (selectedUserId != null && selectedUserUrl != null) {
            onUserClickShowDetailPane(selectedUserId, selectedUserUrl)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
