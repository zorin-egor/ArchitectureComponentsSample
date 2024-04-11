package com.sample.architecturecomponents.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.ui.widgets.CircularContent
import com.sample.architecturecomponents.feature.details.widgets.DetailsContent
import timber.log.Timber

@Composable
internal fun DetailsScreen(
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Timber.d("DetailsScreen()")

    val detailsState: DetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: DetailsActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = detailsAction) {
        is DetailsActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Timber.d("DetailsScreen() - state: $detailsState, $detailsAction")

    when (val state = detailsState) {
        DetailsUiState.Loading -> CircularContent()
        is DetailsUiState.Success -> DetailsContent(
            state = state,
            onUrlClick = onUrlClick,
        )
    }
}