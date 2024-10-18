package com.sample.architecturecomponents.feature.users

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import com.sample.architecturecomponents.core.ui.R
import com.sample.architecturecomponents.core.ui.ext.getErrorMessage
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.core.ui.widgets.ListContentWidget
import com.sample.architecturecomponents.core.ui.widgets.SimplePlaceholderContent
import com.sample.architecturecomponents.feature.users.models.UsersActions
import com.sample.architecturecomponents.feature.users.models.UsersEvents
import com.sample.architecturecomponents.feature.users.widgets.UsersItemContent
import timber.log.Timber

@Composable
fun UsersScreen(
    onUserClick: (Long, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    Timber.d("UsersScreen()")

    val context = LocalContext.current
    val usersUiState by viewModel.state.collectAsStateWithLifecycle()
    val usersAction by viewModel.action.collectAsStateWithLifecycle()
    val nextUsers: () -> Unit = remember {{ viewModel.setEvent(UsersEvents.NextUser) }}

    Timber.d("UsersScreen() - state: $usersUiState, $usersAction")

    when(val state = usersUiState) {
        UiState.Loading -> CircularContent()

        UiState.Empty -> SimplePlaceholderContent(
            header = R.string.empty_placeholder_header,
            title = R.string.empty_placeholder_title,
            image = AppIcons.Empty,
            imageContentDescription = R.string.empty_placeholder_header
        )

        is UiState.Success -> {
            ListContentWidget(
                items = state.item.users,
                onKey = { it.id.toString() },
                onBottomEvent = nextUsers,
                isBottomProgress = state.item.isBottomProgress
            ) { user ->
                UsersItemContent(
                    user = user,
                    onEventAction = viewModel::setEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }

        else -> {}
    }

    when(val action = usersAction) {
        UsersActions.None -> {}

        is UsersActions.NavigateToDetails -> {
            onUserClick(action.id, action.url)
            viewModel.setEvent(UsersEvents.None)
        }

        is UsersActions.ShowError -> {
            Timber.d("User action: $action")
            LaunchedEffect(key1 = action) {
                onShowSnackbar(context.getErrorMessage(action.error), null)
                viewModel.setEvent(UsersEvents.None)
            }
        }

    }
}

