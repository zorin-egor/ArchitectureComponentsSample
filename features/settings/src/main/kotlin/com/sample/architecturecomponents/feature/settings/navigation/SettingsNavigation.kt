package com.sample.architecturecomponents.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sample.architecturecomponents.feature.settings.SettingsScreen


const val SETTINGS_ROUTE = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = SETTINGS_ROUTE,
    ) {
        SettingsScreen()
    }
}