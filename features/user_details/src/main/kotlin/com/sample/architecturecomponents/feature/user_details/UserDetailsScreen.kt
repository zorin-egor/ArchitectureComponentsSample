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
import com.sample.architecturecomponents.feature.user_details.widgets.UserDetailsContent
import timber.log.Timber

@Composable
internal fun UserDetailsScreen(
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    Timber.d("UserDetailsScreen($viewModel)")

    val detailsState: UserDetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: UserDetailsActions? by viewModel.action.collectAsStateWithLifecycle()

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
            modifier = Modifier.fillMaxSize().padding(8.dp)
        )
    }
}