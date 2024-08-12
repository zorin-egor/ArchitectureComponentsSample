package com.sample.architecturecomponents.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.sample.architecturecomponents.app.navigation.AppNavHost
import com.sample.architecturecomponents.app.navigation.TopLevelDestination
import com.sample.architecturecomponents.core.designsystem.component.AppBackground
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationBar
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationBarItem
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationRail
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationRailItem
import com.sample.architecturecomponents.core.designsystem.component.AppTopBar
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.ui.ext.rootViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationState
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationViewModel
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_DETAILS_ROUTE_PATH
import com.sample.architecturecomponents.feature.settings.navigation.SETTINGS_ROUTE
import com.sample.architecturecomponents.feature.themes.ThemesDialog
import com.sample.architecturecomponents.feature.user_details.navigation.USER_DETAILS_ROUTE_PATH
import timber.log.Timber
import com.sample.architecturecomponents.app.R as AppR
import com.sample.architecturecomponents.core.ui.R as CoreUiR
import com.sample.architecturecomponents.feature.repository_details.R as RepoDetailsR
import com.sample.architecturecomponents.feature.settings.R as SettingsR
import com.sample.architecturecomponents.feature.user_details.R as UserDetailsR

@OptIn(ExperimentalComposeUiApi::class,)
@Composable
fun AppRoot(appState: AppState) {
    AppBackground {
        Timber.d("AppRoot($appState)")

        val snackbarHostState = remember { SnackbarHostState() }
        var showThemesDialog by rememberSaveable { mutableStateOf(false) }
        val isOffline by appState.isOffline.collectAsStateWithLifecycle()

        val notConnectedMessage = stringResource(AppR.string.not_connected)
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = SnackbarDuration.Long,
                )
            }
        }

        if (showThemesDialog) {
            ThemesDialog(
                onDismiss = { showThemesDialog = false },
            )
        }

        Scaffold(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    AppBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("AppBottomBar"),
                    )
                }
            }
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                if (appState.shouldShowNavRail) {
                    AppNavRail(
                        destinations = appState.topLevelDestinations,
                        destinationsWithUnreadResources = setOf(TopLevelDestination.SETTINGS),
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier
                            .testTag("AppNavRail")
                            .safeDrawingPadding(),
                    )
                }

                Column(Modifier.fillMaxSize()) {
                    NavAppTopBar(state = appState)

                    AppNavHost(
                        appState = appState,
                        showThemeDialog = { showThemesDialog = true },
                        onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = SnackbarDuration.Short,
                            ) == SnackbarResult.ActionPerformed
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NavAppTopBar(
    state: AppState,
) {
    val viewModel: TopBarNavigationViewModel? = rootViewModel()

    var isTopBarVisible = true
    var toolbarTitle: Int? = null
    var navigationIcon: ImageVector? = null
    var navigationDesc: String? = null
    var actionIcon: ImageVector? = null
    var actionDesc: String? = null
    var actionClick: () -> Unit = {}
    val backClick: () -> Unit = { state.navController.navigateUp() }

    Timber.d("NavAppTopBar() - ${state.shouldShowBottomBar}")

    when (val route = state.currentDestination?.route) {
        USER_DETAILS_ROUTE_PATH -> {
            toolbarTitle = UserDetailsR.string.feature_user_details_title
            navigationIcon = Icons.ArrowBack
            navigationDesc = stringResource(UserDetailsR.string.feature_user_details_title)
            actionIcon = Icons.Share
            actionDesc = stringResource(id = CoreUiR.string.share)
            actionClick = { viewModel?.emit(route, TopBarNavigationState.Menu) }
            isTopBarVisible = true
        }
        REPOSITORY_DETAILS_ROUTE_PATH -> {
            toolbarTitle = RepoDetailsR.string.feature_repository_details_title
            navigationIcon = Icons.ArrowBack
            navigationDesc = stringResource(RepoDetailsR.string.feature_repository_details_title)
            isTopBarVisible = true
        }
        SETTINGS_ROUTE -> {
            toolbarTitle = SettingsR.string.feature_settings_title
            isTopBarVisible = true
        }
        else -> {
            isTopBarVisible = false
        }
    }

    if (!isTopBarVisible) {
        return
    }

    AppTopBar(
        titleRes = toolbarTitle,
        navigationIcon = navigationIcon,
        navigationIconContentDescription = navigationDesc,
        actionIcon = actionIcon,
        actionIconContentDescription = actionDesc,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
        onActionClick = actionClick,
        onNavigationClick = backClick,
    )
}

@Composable
private fun AppBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    AppNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            AppNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun AppNavRail(
    destinations: List<TopLevelDestination>,
    destinationsWithUnreadResources: Set<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    AppNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            val hasUnread = destinationsWithUnreadResources.contains(destination)
            AppNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = if (hasUnread) Modifier.notificationDot() else Modifier,
            )
        }
    }
}

private fun Modifier.notificationDot(): Modifier =
    composed {
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        drawWithContent {
            drawContent()
            drawCircle(
                tertiaryColor,
                radius = 5.dp.toPx(),
                center = center + Offset(
                    64.dp.toPx() * .45f,
                    32.dp.toPx() * -.45f - 6.dp.toPx(),
                ),
            )
        }
    }

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy
        ?.any { destination.route.equals(other = it.route, ignoreCase = true) }
        ?: false
