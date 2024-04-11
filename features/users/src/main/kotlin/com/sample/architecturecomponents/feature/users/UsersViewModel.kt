package com.sample.architecturecomponents.feature.users

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.domain.GetUsersUseCase
import com.sample.architecturecomponents.core.model.User
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject
import com.sample.architecturecomponents.core.ui.R as CoreUiR

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getSearchContentsUseCase: GetUsersUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<UsersUiState>(UsersUiState.Loading)

    val state: StateFlow<UsersUiState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<UsersActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<UsersActions> = _action.asSharedFlow()

    private var usersJob: Job? = null

    init {
        usersJob = getSearchContentsUseCase()
            .getUsers()
    }

    private fun Flow<List<User>>.getUsers(): Job =
        catch {
            val error = it.message ?: context.getString(CoreUiR.string.error_unknown)
            Timber.e(error)
            _action.emit(UsersActions.ShowError(error))
            _state.value.setBottomProgress(false)
        }
        .map { UsersUiState.Success(users = it, isBottomProgress = false) }
        .onEach {
            _state.emit(it)
            delay(1000)
        }
        .launchIn(scope = viewModelScope)

    fun nextUsers() {
        if (usersJob?.isActive == true) return
        usersJob = getSearchContentsUseCase()
            .onStart {
                Timber.d("nextUsers() - onStart")
                _state.value.setBottomProgress(true)
            }
            .getUsers()
    }

    private suspend fun UsersUiState.setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        if (prevState is UsersUiState.Success) {
            _state.emit(prevState.copy(isBottomProgress = isBottomProgress))
        }
    }
}