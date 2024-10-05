package com.sample.architecturecomponents.feature.repository_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.feature.repository_details.navigation.RepositoryDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRepositoryDetailsByIdUseCase: GetRepositoryDetailsByOwnerUseCase,
) : ViewModel() {

    private val repositoryArgs: RepositoryDetailsArgs = RepositoryDetailsArgs(savedStateHandle)

    private val owner = repositoryArgs.owner

    private val repo = repositoryArgs.repo

    private val _action = MutableSharedFlow<RepositoryDetailsActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<RepositoryDetailsActions> = _action.asSharedFlow()

    var repositoryDetails: RepositoryDetails? = null
        private set

    val state: StateFlow<RepositoryDetailsUiState> = getRepositoryDetailsByIdUseCase(owner = owner, repo = repo)
        .mapNotNull { item ->
            when(item) {
                Result.Loading -> null
                is Result.Error -> throw item.exception
                is Result.Success -> {
                    repositoryDetails = item.data
                    RepositoryDetailsUiState.Success(repositoryDetails = item.data)
                }
            }
        }
        .catch {
            Timber.e(it)
            _action.emit(RepositoryDetailsActions.ShowError(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepositoryDetailsUiState.Loading,
        )
}