package com.sample.architecturecomponents.feature.user_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.ui.ext.rootViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationState
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationViewModel
import com.sample.architecturecomponents.feature.user_details.navigation.USER_DETAILS_ROUTE_PATH
import com.sample.architecturecomponents.feature.user_details.widgets.UserDetailsContent
import timber.log.Timber

@Composable
internal fun UserDetailsScreen(
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = hiltViewModel(),
    topBarViewModel: TopBarNavigationViewModel? = rootViewModel()
) {
    Timber.d("UserDetailsScreen($viewModel, $topBarViewModel)")

    val detailsState: UserDetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: UserDetailsActions? by viewModel.action.collectAsStateWithLifecycle()

    val topBarNavigationState = topBarViewModel?.collect(
        key = USER_DETAILS_ROUTE_PATH,
        initialValue = TopBarNavigationState.None
    )?.collectAsStateWithLifecycle(initialValue = TopBarNavigationState.None)

    when(val action = topBarNavigationState?.value) {
       is TopBarNavigationState.Menu -> {}
       else -> Timber.d("Top bar navigation: $action")
    }

    when(val action = detailsAction) {
        is UserDetailsActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Timber.d("UserDetailsScreen() - state: $detailsState, $detailsAction")

    when (val state = detailsState) {
        UserDetailsUiState.Loading -> CircularContent()
        is UserDetailsUiState.Success -> UserDetailsContent(
            userDetails = state.userDetails,
            onUrlClick = onUrlClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}