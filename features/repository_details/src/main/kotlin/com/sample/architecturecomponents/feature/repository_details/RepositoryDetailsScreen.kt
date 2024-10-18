package com.sample.architecturecomponents.feature.repository_details

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.common.extensions.getShareIntent
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.ui.ext.getErrorMessage
import com.sample.architecturecomponents.core.ui.ext.rootViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationState
import com.sample.architecturecomponents.core.ui.viewmodels.TopBarNavigationViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.feature.repository_details.models.RepositoryDetailsActions
import com.sample.architecturecomponents.feature.repository_details.models.RepositoryDetailsEvents
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

    val onShareClick: (String) -> Unit = remember {{
        resultActivity.launch(
            input = context.getShareIntent(
                body = it,
                title = context.getString(R.string.feature_repository_details_share_title)
            ),
        )
    }}

    val detailsState by viewModel.state.collectAsStateWithLifecycle()
    val detailsAction by viewModel.action.collectAsStateWithLifecycle()

    val topBarNavigationState = topBarViewModel?.collect(key = REPOSITORY_DETAILS_ROUTE_PATH)
        ?.collectAsStateWithLifecycle(initialValue = TopBarNavigationState.None)

    when(val action = topBarNavigationState?.value) {
        is TopBarNavigationState.Menu -> {
            Timber.d("TopBarNavigationState: menu")
            viewModel.setEvent(RepositoryDetailsEvents.ShareProfile)
            topBarViewModel.emit(REPOSITORY_DETAILS_ROUTE_PATH, TopBarNavigationState.None)
        }
        is TopBarNavigationState.Back -> {
            viewModel.setEvent(RepositoryDetailsEvents.NavigationBack)
        }
        else -> Timber.d("TopBarNavigationState: $action")
    }

    when(val action = detailsAction) {
        is RepositoryDetailsActions.ShareUrl -> {
            onShareClick(action.url)
            viewModel.setEvent(RepositoryDetailsEvents.None)
        }
        is RepositoryDetailsActions.ShowError -> {
            LaunchedEffect(key1 = action) {
                onShowSnackbar(context.getErrorMessage(action.error), null)
                viewModel.setEvent(RepositoryDetailsEvents.None)
            }
        }
        else -> {}
    }

    Timber.d("RepositoryDetailsScreen() - state: $detailsState, $detailsAction")

    when (val state = detailsState) {
        UiState.Loading -> CircularContent()
        is UiState.Success -> RepositoryDetailsContent(
            isTopBarVisible = isTopBarVisible,
            repositoryDetails = state.item,
            onEventAction = viewModel::setEvent,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        )
        else -> {}
    }
}