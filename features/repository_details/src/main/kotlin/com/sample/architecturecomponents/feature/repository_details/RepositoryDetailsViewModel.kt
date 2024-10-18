package com.sample.architecturecomponents.feature.repository_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.core.ui.viewmodels.UiStateViewModel
import com.sample.architecturecomponents.feature.repository_details.models.RepositoryDetailsActions
import com.sample.architecturecomponents.feature.repository_details.models.RepositoryDetailsEvents
import com.sample.architecturecomponents.feature.repository_details.navigation.RepositoryDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRepositoryDetailsByIdUseCase: GetRepositoryDetailsByOwnerUseCase,
) : UiStateViewModel<RepositoryDetails, RepositoryDetailsActions, RepositoryDetailsEvents>(
    initialAction = RepositoryDetailsActions.None
) {

    private val repositoryArgs: RepositoryDetailsArgs = RepositoryDetailsArgs(savedStateHandle)

    private val owner = repositoryArgs.owner

    private val repo = repositoryArgs.repo

    private var repositoryDetails: RepositoryDetails? = null

    override val state: StateFlow<UiState<RepositoryDetails>> = getRepositoryDetailsByIdUseCase(owner = owner, repo = repo)
        .mapNotNull { item ->
            when(item) {
                Result.Loading -> UiState.Loading

                is Result.Error -> {
                    getLastSuccessStateOrNull<RepositoryDetails>()?.let {
                        setAction(RepositoryDetailsActions.ShowError(item.exception))
                        return@mapNotNull null
                    } ?: UiState.Empty
                }

                is Result.Success -> {
                    repositoryDetails = item.data
                    UiState.Success(item.data)
                }
            }
        }.catch { error ->
            Timber.e(error)
            setAction(RepositoryDetailsActions.ShowError(error))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading,
        )

    override fun setEvent(item: RepositoryDetailsEvents) {
        when(item) {
            RepositoryDetailsEvents.None -> setAction(RepositoryDetailsActions.None)
            RepositoryDetailsEvents.NavigationBack -> { /* For analytic */ }
            RepositoryDetailsEvents.ShareProfile -> repositoryDetails?.let { setAction(RepositoryDetailsActions.ShareUrl(it.htmlUrl)) }
        }
    }
}