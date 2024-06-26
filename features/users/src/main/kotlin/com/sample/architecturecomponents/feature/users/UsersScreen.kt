package com.sample.architecturecomponents.feature.users

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.ui.R
import com.sample.architecturecomponents.core.ui.widgets.ListContentWidget
import com.sample.architecturecomponents.core.ui.widgets.SimplePlaceholderContent
import com.sample.architecturecomponents.feature.users.widgets.UsersItemContent
import timber.log.Timber

@Composable
internal fun UsersScreen(
    onUserClick: (Long, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    Timber.d("UsersScreen()")

    val usersUiState: UsersUiState by viewModel.state.collectAsStateWithLifecycle()
    val usersAction: UsersActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = usersAction) {
        is UsersActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    when(val state = usersUiState) {
        UsersUiState.Loading -> CircularContent()
        UsersUiState.Empty -> SimplePlaceholderContent(
            header = R.string.empty_placeholder_header,
            title = R.string.empty_placeholder_title,
            image = Icons.Empty,
            imageContentDescription = R.string.empty_placeholder_header
        )
        is UsersUiState.Success -> {
            ListContentWidget(
                items = state.users,
                onKey = { it.id.toString() },
                onItemClick = { onUserClick(it.id, it.url) },
                onBottomEvent = viewModel::nextUsers,
                isBottomProgress = state.isBottomProgress
            ) { user, onClick ->
                UsersItemContent(
                    user = user,
                    onUserClick = onClick,
                    modifier = Modifier.fillMaxWidth()
                        .height(250.dp)
                )
            }
        }
    }
}

