package com.sample.architecturecomponents.feature.repository_details

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
import com.sample.architecturecomponents.feature.repository_details.widgets.RepositoryDetailsContent
import timber.log.Timber

@Composable
internal fun RepositoryDetailsScreen(
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: RepositoryDetailsViewModel = hiltViewModel()
) {
    Timber.d("RepositoryDetailsScreen()")

    val detailsState: RepositoryDetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: RepositoryDetailsActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = detailsAction) {
        is RepositoryDetailsActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Timber.d("RepositoryDetailsScreen() - state: $detailsState, $detailsAction")

    when (val state = detailsState) {
        RepositoryDetailsUiState.Loading -> CircularContent()
        is RepositoryDetailsUiState.Success -> RepositoryDetailsContent(
            repositoryDetails = state.repositoryDetails,
            onUrlClick = onUrlClick,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        )
    }
}