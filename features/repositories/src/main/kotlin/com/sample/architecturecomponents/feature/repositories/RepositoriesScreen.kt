package com.sample.architecturecomponents.feature.repositories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.designsystem.component.CircularContent
import com.sample.architecturecomponents.core.designsystem.component.SearchToolbar
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.ui.widgets.ListContentWidget
import com.sample.architecturecomponents.core.ui.widgets.SimplePlaceholderContent
import com.sample.architecturecomponents.feature.repositories.widgets.RepositoriesItemContent
import timber.log.Timber

import com.sample.architecturecomponents.core.ui.R as CoreUiR

@Composable
internal fun RepositoriesScreen(
    onRepositoryClick: (Long) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: RepositoriesViewModel = hiltViewModel(),
) {
    Timber.d("UsersScreen()")

    val reposUiState: RepositoriesByNameUiState by viewModel.state.collectAsStateWithLifecycle()
    val reposAction: RepositoriesActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = reposAction) {
        is RepositoriesActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        SearchToolbar(
            searchQuery = reposUiState.query,
            contentDescriptionBack = "contentDescriptionBack",
            contentDescriptionSearch = "contentDescriptionSearch",
            contentDescriptionClose = "contentDescriptionClose",
            onSearchQueryChanged = viewModel::queryRepositories,
            onSearchTriggered = {},
            modifier = Modifier.wrapContentHeight(),
            placeholder = R.string.feature_repositories_by_name_search_title,
            isFocusRequest = false
        )

        when(val state = reposUiState.state) {
            RepositoriesByNameUiStates.Loading -> CircularContent()
            RepositoriesByNameUiStates.Empty -> SimplePlaceholderContent(
                header = CoreUiR.string.empty_placeholder_header,
                title = CoreUiR.string.empty_placeholder_title,
                image = Icons.Empty,
                imageContentDescription = CoreUiR.string.empty_placeholder_header
            )
            RepositoriesByNameUiStates.Start -> SimplePlaceholderContent(
                header = CoreUiR.string.search_placeholder_header,
                title = CoreUiR.string.search_placeholder_title,
                image = Icons.Search,
                imageContentDescription = CoreUiR.string.search_placeholder_header
            )
            is RepositoriesByNameUiStates.Success -> {
                ListContentWidget(
                    items = state.repositories,
                    onKey = { it.id.toString() },
                    onItemClick = { onRepositoryClick(it.id) },
                    onBottomEvent = viewModel::nextRepositories,
                    isBottomProgress = state.isBottomProgress
                ) { repository, onClick ->
                    RepositoriesItemContent(
                        repository = repository,
                        onRepositoryClick = onClick,
                        modifier = Modifier.fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }
        }
    }

}

