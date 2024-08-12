package com.sample.architecturecomponents.feature.repository_details

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
import com.sample.architecturecomponents.core.ui.ext.rootViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationState
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationViewModel
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_DETAILS_ROUTE_PATH
import com.sample.architecturecomponents.feature.repository_details.widgets.RepositoryDetailsContent
import timber.log.Timber

@Composable
internal fun RepositoryDetailsScreen(
    isTopBarVisible: Boolean,
    onUrlClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: RepositoryDetailsViewModel = hiltViewModel(),
    topBarViewModel: TopBarNavigationViewModel? = rootViewModel()
) {
    Timber.d("RepositoryDetailsScreen()")

    val context = LocalContext.current
    val resultActivity = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        Timber.d("rememberLauncherForActivityResult($result)")
    }

    val detailsState: RepositoryDetailsUiState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction: RepositoryDetailsActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val topBarNavigationState = topBarViewModel?.collect(key = REPOSITORY_DETAILS_ROUTE_PATH)
        ?.collectAsStateWithLifecycle(initialValue = TopBarNavigationState.None)

    when(val action = topBarNavigationState?.value) {
        is TopBarNavigationState.Menu -> {
            Timber.d("TopBarNavigationState: menu")
            viewModel.repositoryDetails?.htmlUrl?.let {
                resultActivity.launch(
                    input = context.getShareIntent(
                        body = it,
                        title = stringResource(id = R.string.feature_repository_details_share_title)
                    ),
                )
            }
            topBarViewModel.emit(REPOSITORY_DETAILS_ROUTE_PATH, TopBarNavigationState.None)
        }
        else -> Timber.d("TopBarNavigationState: $action")
    }

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
            isTopBarVisible = isTopBarVisible,
            repositoryDetails = state.repositoryDetails,
            onUrlClick = onUrlClick,
            onShareClick = {
                resultActivity.launch(
                    input = context.getShareIntent(
                        body = it.htmlUrl,
                        title = context.getString(R.string.feature_repository_details_share_title)
                    ),
                )
            },
            modifier = Modifier.fillMaxSize().padding(8.dp)
        )
    }
}