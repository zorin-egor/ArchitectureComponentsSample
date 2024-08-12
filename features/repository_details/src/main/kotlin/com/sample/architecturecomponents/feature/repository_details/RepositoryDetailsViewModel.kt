package com.sample.architecturecomponents.feature.repository_details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.feature.repository_details.navigation.RepositoryDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import com.sample.architecturecomponents.core.ui.R as CoreUiR

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRepositoryDetailsByIdUseCase: GetRepositoryDetailsByOwnerUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val repositoryArgs: RepositoryDetailsArgs = RepositoryDetailsArgs(savedStateHandle)

    private val owner = repositoryArgs.owner

    private val repo = repositoryArgs.repo

    private val _action = MutableSharedFlow<RepositoryDetailsActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<RepositoryDetailsActions> = _action.asSharedFlow()

    var repositoryDetails: RepositoryDetails? = null
        private set

    val state: StateFlow<RepositoryDetailsUiState> = getRepositoryDetailsByIdUseCase(owner = owner, repo = repo)
        .map {
            val item = it.getOrNull()
            val error = it.exceptionOrNull()
            when {
                it.isSuccess && item != null -> {
                    repositoryDetails = item
                    RepositoryDetailsUiState.Success(repositoryDetails = item)
                }
                it.isSuccess && item == null -> RepositoryDetailsUiState.Loading
                it.isFailure && error != null -> throw error
                else -> throw IllegalStateException("Unknown state")
            }
        }
        .catch {
            val error = it.message ?: context.getString(CoreUiR.string.error_unknown)
            Timber.e(error)
            _action.emit(RepositoryDetailsActions.ShowError(error))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepositoryDetailsUiState.Loading,
        )
}