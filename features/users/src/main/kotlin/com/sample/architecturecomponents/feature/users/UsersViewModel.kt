package com.sample.architecturecomponents.feature.users

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUsersUseCase
import com.sample.architecturecomponents.core.model.User
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
import timber.log.Timber
import javax.inject.Inject

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
        usersJob = getSearchContentsUseCase(id = GetUsersUseCase.SINCE_ID)
            .getUsers()
    }

    private fun Flow<Result<List<User>>>.getUsers(): Job =
        mapNotNull { item ->
            when(item) {
                Result.Loading -> UsersUiState.Loading
                is Result.Error -> throw item.exception
                is Result.Success -> UsersUiState.Success(users = item.data, isBottomProgress = false)
            }
        }
        .onEach {
            Timber.d("Flow.getUsers() - onEach($it)")
            _state.emit(it)
            delay(500)
        }
        .catch {
            Timber.e(it)
            when(it) {
                EmptyException -> _state.emit(UsersUiState.Empty)
                else -> _action.emit(UsersActions.ShowError(context.getErrorMessage(it)))
            }
            setBottomProgress(false)
        }
        .launchIn(scope = viewModelScope)

    fun nextUsers() {
        if (usersJob?.isActive == true) return
        usersJob = getSearchContentsUseCase()
            .onStart {
                Timber.d("nextUsers() - onStart")
                setBottomProgress(true)
            }
            .getUsers()
    }

    private suspend fun setBottomProgress(isBottomProgress: Boolean) {
        val prevState = _state.value
        if (prevState is UsersUiState.Success) {
            Timber.d("setBottomProgress($prevState)")
            _state.emit(prevState.copy(isBottomProgress = isBottomProgress))
        }
    }
}