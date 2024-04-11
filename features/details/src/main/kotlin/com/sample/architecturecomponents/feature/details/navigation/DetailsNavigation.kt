package com.sample.architecturecomponents.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sample.architecturecomponents.feature.details.DetailsScreen
import org.jetbrains.annotations.VisibleForTesting
import timber.log.Timber
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val USER_ID_ARG = "userId"
internal const val USER_URL_ARG = "userUrl"
internal const val DETAILS_ROUTE_ = "details_route"
const val DETAILS_ROUTE = "$DETAILS_ROUTE_?$USER_ID_ARG={$USER_ID_ARG}&$USER_URL_ARG={$USER_URL_ARG}"

internal class DetailsArgs(val userUrl: String, val userId: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[USER_URL_ARG]), URL_CHARACTER_ENCODING),
                    checkNotNull(savedStateHandle[USER_ID_ARG])
            )
}

fun NavController.navigateToDetails(userId: Long, userUrl: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    val encodedUrl = URLEncoder.encode(userUrl, URL_CHARACTER_ENCODING)
    val newRoute = "$DETAILS_ROUTE_?$USER_ID_ARG=$userId&$USER_URL_ARG=$encodedUrl"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.detailsScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = DETAILS_ROUTE,
        arguments = listOf(
            navArgument(USER_ID_ARG) { type = NavType.LongType },
            navArgument(USER_URL_ARG) { type = NavType.StringType },
        ),
    ) {
        Timber.d("DetailsScreen() - detailsScreen")
        DetailsScreen(
            onUrlClick = onUrlClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}