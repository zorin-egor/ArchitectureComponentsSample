package com.sample.architecturecomponents.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.app.navigation.NavHost
import com.sample.architecturecomponents.core.designsystem.component.AppBackground
import com.sample.architecturecomponents.core.designsystem.component.AppTopBar
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.feature.details.navigation.DETAILS_ROUTE
import com.sample.architecturecomponents.feature.settings.SettingsDialog
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import timber.log.Timber
import com.sample.architecturecomponents.app.R as AppR
import com.sample.architecturecomponents.feature.details.R as DetailsR
import com.sample.architecturecomponents.feature.users.R as UsersR

@OptIn(ExperimentalComposeUiApi::class,)
@Composable
fun AppRoot(appState: AppState) {
    AppBackground {
        Timber.d("AppRoot($appState)")

        val showSettingsDialog = rememberSaveable { mutableStateOf(false) }
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

        if (showSettingsDialog.value) {
            SettingsDialog(
                onDismiss = { showSettingsDialog.value = false },
            )
        }

        Scaffold(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
            ) {

                NavAppTopBar(state = appState, settingsState = showSettingsDialog)

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
private fun NavAppTopBar(state: AppState, settingsState: MutableState<Boolean>) {
    var toolbarTitle: Int? = null
    var navigationIcon: ImageVector? = null
    var navigationDesc: String? = null
    var actionIcon: ImageVector? = null
    var actionDesc: String? = null
    var actionClick: () -> Unit = {}
    var navigationClick: () -> Unit = {}

    when(val route = state.currentDestination?.route) {
        USERS_ROUTE -> {
            toolbarTitle = UsersR.string.users_title
            actionIcon = Icons.Settings
            actionDesc = stringResource(DetailsR.string.details_title)
            actionClick = { settingsState.value = true }
        }
        DETAILS_ROUTE -> {
            toolbarTitle = DetailsR.string.details_title
            navigationIcon = Icons.ArrowBack
            navigationDesc = stringResource(DetailsR.string.details_title)
            actionDesc = stringResource(DetailsR.string.details_title)
            navigationClick = { state.navController.popBackStack() }
        }
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
