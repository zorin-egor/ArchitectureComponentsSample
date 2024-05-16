package com.sample.architecturecomponents.feature.repositories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.domain.usecases.GetRecentSearchUseCase
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoriesByNameUseCase
import com.sample.architecturecomponents.core.domain.usecases.SetRecentSearchUseCase
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class RepositoriesViewModel @Inject constructor(
    private val getReposByNameUseCase: GetRepositoriesByNameUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val setRecentSearchUseCase: SetRecentSearchUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<RepositoriesByNameUiState>(RepositoriesByNameUiState(
        query = "",
        recentSearch = emptyList(),
        state = RepositoriesByNameUiStates.Start
    ))

    val state: StateFlow<RepositoriesByNameUiState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<RepositoriesActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<RepositoriesActions> = _action.asSharedFlow()
    private val previousQuery: String get() = _state.value.query

    private var recentSearchJob: Job? = null
    private var organizationsJob: Job? = null

    private fun Flow<List<Repository>>.getOrganizations(query: String): Job =
        catch {
            val error = context.getErrorMessage(it)
            Timber.e(error)

            when(it) {
                EmptyException -> _state.emit(RepositoriesByNameUiState(
                    query = query,
                    recentSearch = emptyList(),
                    state = RepositoriesByNameUiStates.Empty
                ))
                else -> _action.emit(RepositoriesActions.ShowError(error))
            }

            setBottomProgress(false)
        }
        .map {
            _state.value.copy(
                query = query,
                state = if (it.isNotEmpty()) {
                    setRecentSearchUseCase(query = query, tag = RecentSearchTags.Repositories)
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

        if (name.trim().isEmpty()) {
            _state.tryEmit(RepositoriesByNameUiState(
                query = "",
                recentSearch = emptyList(),
                state = RepositoriesByNameUiStates.Start
            ))
            return
        }

        if (name == _state.value.query) {
            Timber.d("queryRepositories() - previous name: ${_state.value.query}")
            return
        }

        setQuery(name)

        recentSearchJob?.cancel()
        recentSearchJob = viewModelScope.launch {
            getRecentSearchUseCase(query = name, tag = RecentSearchTags.Repositories)
                .collect {
                    Timber.d("getRecentSearchUseCase() - collect($it)")
                    setRecentSearch(items = mapTo(it))
                }

            organizationsJob = getReposByNameUseCase(name = name)
                .onStart {
                    Timber.d("queryRepositories() - onStart")
                    _state.emit(_state.value.copy(
                        query = name,
                        state = RepositoriesByNameUiStates.Loading
                    ))
                }
                .getOrganizations(query = name)
        }
    }

    fun nextRepositories() {
        if (organizationsJob?.isActive == true) return
        organizationsJob = getReposByNameUseCase()
            .onStart {
                Timber.d("nextRepositories() - onStart")
                setBottomProgress(true)
            }
            .getOrganizations(query = previousQuery)
    }

    private suspend fun setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        val prevStates = prevState.state
        if (prevStates is RepositoriesByNameUiStates.Success) {
            _state.emit(prevState.copy(state = prevStates.copy(isBottomProgress = isBottomProgress)))
        }
    }

    private suspend fun setRecentSearch(items: List<Pair<String, String>>) {
        _state.emit(_state.value.copy(recentSearch = items))
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}

private fun mapTo(item: RecentSearch): Pair<String, String> =
    Pair(first = item.value, second = item.tag.name)

private fun mapTo(item: List<RecentSearch>): List<Pair<String, String>> =
    item.map(::mapTo)