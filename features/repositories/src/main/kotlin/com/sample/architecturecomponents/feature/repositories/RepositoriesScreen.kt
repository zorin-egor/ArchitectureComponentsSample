package com.sample.architecturecomponents.feature.repositories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.sample.architecturecomponents.core.designsystem.component.ExposedTextField
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import com.sample.architecturecomponents.core.ui.ext.getErrorMessage
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.core.ui.widgets.ListContentWidget
import com.sample.architecturecomponents.core.ui.widgets.SimplePlaceholderContent
import com.sample.architecturecomponents.feature.repositories.models.RepositoriesActions
import com.sample.architecturecomponents.feature.repositories.models.RepositoriesEvents
import com.sample.architecturecomponents.feature.repositories.widgets.RepositoriesItemContent
import timber.log.Timber
import com.sample.architecturecomponents.core.ui.R as CoreUiR

@Composable
fun RepositoriesScreen(
    onRepositoryClick: (String, String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    onSearchClear: (() -> Unit)? = null,
    viewModel: RepositoriesViewModel = hiltViewModel(),
) {
    Timber.d("RepositoriesScreen()")

    val context = LocalContext.current
    val onNextRepositories = remember {{ viewModel.setEvent(RepositoriesEvents.NextRepositories) }}
    val reposUiState by viewModel.state.collectAsStateWithLifecycle()
    val reposAction by viewModel.action.collectAsStateWithLifecycle()

    when(val action = reposAction) {
        is RepositoriesActions.NavigationToDetails -> {
            onRepositoryClick(action.owner, action.name)
            viewModel.setEvent(RepositoriesEvents.None)
        }
        is RepositoriesActions.ShowError -> {
            LaunchedEffect(key1 = action) {
                onShowSnackbar(context.getErrorMessage(action.error), null)
                viewModel.setEvent(RepositoriesEvents.None)
            }
        }
        else -> {}
    }

    Timber.d("RepositoriesScreen() - state, action: $reposUiState, $reposAction")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val queryState by viewModel.searchQuery.collectAsStateWithLifecycle()
        val searchState = (reposUiState.searchState as? UiState.Success)?.item
        val options = searchState?.recentSearch ?: emptyList()

        ExposedTextField(
            searchQuery = queryState,
            options = options,
            contentDescriptionSearch = "contentDescriptionSearch",
            contentDescriptionClose = "contentDescriptionClose",
            onSearchQueryChanged = {
                Timber.d("RepositoriesScreen() - onSearchQueryChanged: $it")
                viewModel.setEvent(RepositoriesEvents.SearchQuery(it.text))
                if (it.text.isEmpty()) {
                    onSearchClear?.invoke()
                }
            },
            modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            placeholder = R.string.feature_repositories_by_name_search_title,
            isFocusRequest = true,
        )

        when(val state = reposUiState.repoState) {
            UiState.Loading -> CircularContent()
            UiState.Empty -> SimplePlaceholderContent(
                header = CoreUiR.string.empty_placeholder_header,
                title = CoreUiR.string.empty_placeholder_title,
                image = AppIcons.Empty,
                imageContentDescription = CoreUiR.string.empty_placeholder_header
            )
            is UiState.Error -> SimplePlaceholderContent(
                header = CoreUiR.string.search_placeholder_header,
                title = CoreUiR.string.search_placeholder_title,
                image = AppIcons.Search,
                imageContentDescription = CoreUiR.string.search_placeholder_header
            )
            is UiState.Success -> {
                ListContentWidget(
                    items = state.item.repositories,
                    onKey = { it.id.toString() },
                    onBottomEvent = onNextRepositories,
                    isBottomProgress = state.item.isBottomProgress,
                ) { repository ->
                    RepositoriesItemContent(
                        repository = repository,
                        onEventAction = viewModel::setEvent,
                        modifier = Modifier.fillMaxWidth()
                            .height(110.dp)
                    )
                }
            }
        }
    }

}

