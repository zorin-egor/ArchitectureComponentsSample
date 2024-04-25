package com.sample.architecturecomponents.feature.repositories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.domain.GetReposByNameUseCase
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import com.sample.architecturecomponents.core.ui.ext.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class RepositoriesViewModel @Inject constructor(
    private val getReposByNameUseCase: GetReposByNameUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<RepositoriesByNameUiState>(RepositoriesByNameUiState(
        query = "",
        state = RepositoriesByNameUiStates.Start
    ))

    val state: StateFlow<RepositoriesByNameUiState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<RepositoriesActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<RepositoriesActions> = _action.asSharedFlow()
    private val previousQuery: String get() = _state.value.query

    private var organizationsJob: Job? = null

    private fun Flow<List<Repository>>.getOrganizations(query: String, isDelay: Boolean = false): Job =
        catch {
            val error = context.getErrorMessage(it)
            Timber.e(error)

            when(it) {
                EmptyException -> _state.emit(RepositoriesByNameUiState(
                    query = query,
                    state = RepositoriesByNameUiStates.Empty
                ))
                else -> _action.emit(RepositoriesActions.ShowError(error))
            }

            setBottomProgress(false)
        }
        .map {
            RepositoriesByNameUiState(
                query = query,
                state = if (it.isNotEmpty()) {
                    RepositoriesByNameUiStates.Success(repositories = it, isBottomProgress = false)
                } else {
                    RepositoriesByNameUiStates.Empty
                }
            )
        }
        .onEach(_state::emit)
        .launchIn(scope = viewModelScope)

    fun queryRepositories(name: String) {
        Timber.d("queryRepositories($name)")
        organizationsJob?.cancel()

        setQuery(name)

        if (name.trim().isEmpty()) {
            _state.tryEmit(RepositoriesByNameUiState(
                query = "",
                state = RepositoriesByNameUiStates.Start
            ))
            return
        }

        organizationsJob = getReposByNameUseCase(name = name)
            .onStart {
                Timber.d("queryRepositories() - onStart")
                _state.emit(RepositoriesByNameUiState(
                    query = name,
                    state = RepositoriesByNameUiStates.Loading
                ))
            }
            .getOrganizations(query = name)
    }

    fun nextRepositories() {
        if (organizationsJob?.isActive == true) return
        organizationsJob = getReposByNameUseCase()
            .onStart {
                Timber.d("nextRepositories() - onStart")
                setBottomProgress(true)
            }
            .getOrganizations(query = previousQuery, isDelay = true)
    }

    private suspend fun setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        val prevStates = _state.value.state
        if (prevStates is RepositoriesByNameUiStates.Success) {
            _state.emit(prevState.copy(state = prevStates.copy(isBottomProgress = isBottomProgress)))
        }
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}