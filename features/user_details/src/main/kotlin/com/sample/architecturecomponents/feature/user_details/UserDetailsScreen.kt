package com.sample.architecturecomponents.feature.user_details

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.common.extensions.getShareIntent
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.ui.ext.getErrorMessage
import com.sample.architecturecomponents.core.ui.ext.rootViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationState
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationViewModel
import com.sample.architecturecomponents.feature.user_details.navigation.USER_DETAILS_ROUTE_PATH
import com.sample.architecturecomponents.feature.user_details.widgets.UserDetailsContent
import timber.log.Timber

@Composable
internal fun UserDetailsScreen(
    isTopBarVisible: Boolean,
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = hiltViewModel(),
    topBarViewModel: TopBarNavigationViewModel? = rootViewModel()
) {
    Timber.d("UserDetailsScreen($viewModel, $topBarViewModel)")

    val context = LocalContext.current
    val resultActivity = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        Timber.d("rememberLauncherForActivityResult($result)")
    }

    val detailsState: UserDetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: UserDetailsActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val topBarNavigationState = topBarViewModel?.collect(key = USER_DETAILS_ROUTE_PATH)
        ?.collectAsStateWithLifecycle(initialValue = TopBarNavigationState.None)

    when(val action = topBarNavigationState?.value) {
       is TopBarNavigationState.Menu -> {
           Timber.d("TopBarNavigationState: menu")
           viewModel.userDetails?.url?.let {
               resultActivity.launch(
                   input = context.getShareIntent(
                       body = it,
                       title = stringResource(id = R.string.feature_user_details_share_title)
                   ),
               )
           }
           topBarViewModel.emit(USER_DETAILS_ROUTE_PATH, TopBarNavigationState.None)
       }
       else -> Timber.d("TopBarNavigationState: $action")
    }

    when(val action = detailsAction) {
        is UserDetailsActions.ShowError -> {
            LaunchedEffect(key1 = action.error) {
                onShowSnackbar(context.getErrorMessage(action.error), null)
            }
        }
        else -> {}
    }

    Timber.d("UserDetailsScreen() - state: $detailsState, $detailsAction")

    when (val state = detailsState) {
        UserDetailsUiState.Loading -> CircularContent()
        is UserDetailsUiState.Success -> UserDetailsContent(
            isTopBarVisible = isTopBarVisible,
            userDetails = state.userDetails,
            onUrlClick = onUrlClick,
            onShareClick = {
                resultActivity.launch(
                    input = context.getShareIntent(
                        body = it.url,
                        title = context.getString(R.string.feature_user_details_share_title)
                    ),
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}