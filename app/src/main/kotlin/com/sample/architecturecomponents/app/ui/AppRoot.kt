package com.sample.architecturecomponents.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.sample.architecturecomponents.app.navigation.NavHost
import com.sample.architecturecomponents.app.navigation.TopLevelDestination
import com.sample.architecturecomponents.core.designsystem.component.AppBackground
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationBar
import com.sample.architecturecomponents.core.designsystem.component.AppNavigationBarItem
import com.sample.architecturecomponents.core.designsystem.component.AppTopBar
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.feature.settings.navigation.SETTINGS_ROUTE
import com.sample.architecturecomponents.feature.themes.ThemesDialog
import com.sample.architecturecomponents.feature.user_details.navigation.USER_DETAILS_ROUTE
import timber.log.Timber
import com.sample.architecturecomponents.app.R as AppR
import com.sample.architecturecomponents.feature.settings.R as SettingsR
import com.sample.architecturecomponents.feature.user_details.R as DetailsR

@OptIn(ExperimentalComposeUiApi::class,)
@Composable
fun AppRoot(appState: AppState) {
    AppBackground {
        Timber.d("AppRoot($appState)")

        val snackbarHostState = remember { SnackbarHostState() }
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

        Scaffold(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                AppBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier.testTag("NiaBottomBar"),
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
            ) {

                NavAppTopBar(state = appState)

                NavHost(
                   appState = appState,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavAppTopBar(state: AppState) {
    var showThemesDialog by rememberSaveable { mutableStateOf(false) }

    var isTopBarVisible = false
    var toolbarTitle: Int? = null
    var navigationIcon: ImageVector? = null
    var navigationDesc: String? = null
    var actionIcon: ImageVector? = null
    var actionDesc: String? = null
    var actionClick: () -> Unit = {}
    var navigationClick: () -> Unit = {}

    when(val route = state.currentDestination?.route) {
        USER_DETAILS_ROUTE -> {
            toolbarTitle = DetailsR.string.feature_user_details_title
            navigationIcon = Icons.ArrowBack
            navigationDesc = stringResource(DetailsR.string.feature_user_details_title)
            navigationClick = { state.navController.navigateUp() }
            isTopBarVisible = true
        }
        SETTINGS_ROUTE -> {
            toolbarTitle = SettingsR.string.feature_settings_title
            actionIcon = Icons.Themes
            actionDesc = stringResource(SettingsR.string.feature_settings_title)
            actionClick = { showThemesDialog = true }
            isTopBarVisible = true
        }
        else -> {
            isTopBarVisible = false
        }
    }

    if (!isTopBarVisible) {
        return
    }

    if (showThemesDialog) {
        ThemesDialog(
            onDismiss = { showThemesDialog = false },
        )
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
        onNavigationClick = navigationClick,
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

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy
        ?.any { destination.route.equals(other = it.route, ignoreCase = true) }
        ?: false
