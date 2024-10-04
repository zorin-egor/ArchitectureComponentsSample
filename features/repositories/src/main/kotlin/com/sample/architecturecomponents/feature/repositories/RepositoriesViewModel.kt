package com.sample.architecturecomponents.feature.repositories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.designsystem.component.SearchTextDataItem
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
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

    private fun Flow<Result<List<Repository>>>.getOrganizations(query: String): Job =
        mapNotNull { item ->
            val state = when(item) {
                Result.Loading -> return@mapNotNull null
                is Result.Error -> throw item.exception
                is Result.Success -> {
                    setRecentSearchUseCase(query = query, tag = RecentSearchTags.Repositories)
                    RepositoriesByNameUiStates.Success(repositories = item.data, isBottomProgress = false)
                }
            }

            _state.value.copy(
                query = query,
                state =state
            )
        }
        .onEach {
            _state.emit(it)
            delay(500)
        }
        .catch {
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
        .launchIn(scope = viewModelScope)

    fun queryRepositories(name: String) {
        Timber.d("queryRepositories($name)")

        val query = _state.value.query
        if (name == query) {
            Timber.d("queryRepositories() - previous name: $query")
            return
        }

        organizationsJob?.cancel()

        if (name.trim().isEmpty()) {
            _state.tryEmit(RepositoriesByNameUiState(
                query = "",
                recentSearch = emptyList(),
                state = RepositoriesByNameUiStates.Start
            ))
            return
        }

        setQuery(name)

        recentSearchJob?.cancel()
        recentSearchJob = viewModelScope.launch {
            getRecentSearchUseCase(query = name, tag = RecentSearchTags.Repositories)
                .mapNotNull {
                    when(it) {
                        Result.Loading -> null
                        is Result.Error -> emptyList()
                        is Result.Success -> it.data.let(::mapTo)
                    }
                }
                .catch { Timber.e(it) }
                .collect {
                    Timber.d("getRecentSearchUseCase() - collect($it)")
                    setRecentSearch(items = it)
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

    private suspend fun setRecentSearch(items: List<SearchTextDataItem>) {
        _state.emit(_state.value.copy(recentSearch = items))
    }

    private fun setQuery(query: String) {
        _state.tryEmit(_state.value.copy(query = query))
    }

}

private fun mapTo(item: RecentSearch): SearchTextDataItem =
    SearchTextDataItem(id = item.tag.name, text = item.value)

private fun mapTo(item: List<RecentSearch>): List<SearchTextDataItem> =
    item.map(::mapTo)