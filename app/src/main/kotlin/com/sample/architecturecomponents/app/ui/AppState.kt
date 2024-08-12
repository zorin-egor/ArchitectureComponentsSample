package com.sample.architecturecomponents.app.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sample.architecturecomponents.app.navigation.TopLevelDestination
import com.sample.architecturecomponents.core.network.connection.NetworkMonitor
import com.sample.architecturecomponents.feature.repositories.navigation.REPOSITORIES_ROUTE
import com.sample.architecturecomponents.feature.repositories.navigation.navigateToRepositories
import com.sample.architecturecomponents.feature.settings.navigation.SETTINGS_ROUTE
import com.sample.architecturecomponents.feature.settings.navigation.navigateToSettings
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import com.sample.architecturecomponents.feature.users.navigation.navigateToUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

val NavHostController.currentDestinationFromState: NavDestination?
    @Composable get() = currentBackStackEntryAsState().value?.destination

@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {

    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        AppState(
            windowSizeClass = windowSizeClass,
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentDestinationFromState

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            USERS_ROUTE -> TopLevelDestination.USERS
            REPOSITORIES_ROUTE -> TopLevelDestination.REPOSITORIES
            SETTINGS_ROUTE -> TopLevelDestination.SETTINGS
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.USERS -> navController.navigateToUsers(navOptions = topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettings(navOptions = topLevelNavOptions)
            TopLevelDestination.REPOSITORIES -> navController.navigateToRepositories(navOptions = topLevelNavOptions)
        }
    }

}
