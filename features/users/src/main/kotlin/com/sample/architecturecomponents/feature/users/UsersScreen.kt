package com.sample.architecturecomponents.feature.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.ui.widgets.CircularContent
import com.sample.architecturecomponents.feature.users.widgets.ItemsUsersContent
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
        is UsersUiState.Success -> ItemsUsersContent(
            state = state,
            onUserClick = { onUserClick(it.id, it.url) },
            onBottomEvent = viewModel::nextUsers,
            modifier = modifier
        )
    }
}

