package com.sample.architecturecomponents.feature.user_details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sample.architecturecomponents.feature.user_details.UserDetailsScreen
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

const val USER_ID_ARG = "userId"
const val USER_URL_ARG = "userUrl"
const val USER_DETAILS_ROUTE = "user_details_route"
const val USER_DETAILS_ROUTE_PATH = "$USER_DETAILS_ROUTE?$USER_ID_ARG={$USER_ID_ARG}&$USER_URL_ARG={$USER_URL_ARG}"

class UserDetailsArgs(val userUrl: String, val userId: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[USER_URL_ARG]), URL_CHARACTER_ENCODING),
                    checkNotNull(savedStateHandle[USER_ID_ARG])
            )
}

fun NavController.navigateToUserDetails(userId: Long, userUrl: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    val encodedUrl = URLEncoder.encode(userUrl, URL_CHARACTER_ENCODING)
    val newRoute = "$USER_DETAILS_ROUTE?$USER_ID_ARG=$userId&$USER_URL_ARG=$encodedUrl"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.userDetailsScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = USER_DETAILS_ROUTE_PATH,
        arguments = listOf(
            navArgument(USER_ID_ARG) { type = NavType.LongType },
            navArgument(USER_URL_ARG) { type = NavType.StringType },
        ),
    ) {
        UserDetailsScreen(
            onUrlClick = onUrlClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}