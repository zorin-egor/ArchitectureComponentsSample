package com.sample.architecturecomponents.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.feature.repositories.navigation.REPOSITORIES_ROUTE
import com.sample.architecturecomponents.feature.settings.navigation.SETTINGS_ROUTE
import com.sample.architecturecomponents.feature.users.navigation.USERS_ROUTE
import com.sample.architecturecomponents.feature.repositories.R as ReposR
import com.sample.architecturecomponents.feature.settings.R as SettingsR
import com.sample.architecturecomponents.feature.users.R as UsersR

enum class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
) {
    USERS(
        route = USERS_ROUTE,
        selectedIcon = Icons.Users,
        unselectedIcon = Icons.UsersBorder,
        iconTextId = UsersR.string.feature_users_title,
    ),
    REPOSITORIES(
        route = REPOSITORIES_ROUTE,
        selectedIcon = Icons.Repositories,
        unselectedIcon = Icons.RepositoriesBorder,
        iconTextId = ReposR.string.feature_repositories_title,
    ),
    SETTINGS(
        route = SETTINGS_ROUTE,
        selectedIcon = Icons.Settings,
        unselectedIcon = Icons.SettingsBorder,
        iconTextId = SettingsR.string.feature_settings_title,
    )
}
