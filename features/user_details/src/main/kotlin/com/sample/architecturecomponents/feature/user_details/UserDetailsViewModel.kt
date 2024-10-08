package com.sample.architecturecomponents.feature.user_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUserDetailsUseCase
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.feature.user_details.navigation.UserDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserDetailsUseCase: GetUserDetailsUseCase,
) : ViewModel() {

    private val usersArgs: UserDetailsArgs = UserDetailsArgs(savedStateHandle)

    private val userId = usersArgs.userId

    private val userUrl = usersArgs.userUrl

    var userDetails: UserDetails? = null
        private set

    private val _action = MutableSharedFlow<UserDetailsActions>(replay = 0, extraBufferCapacity = 1)

    val action: Flow<UserDetailsActions?> = _action.asSharedFlow()

    val state: StateFlow<UserDetailsUiState> = getUserDetailsUseCase(userId = userId, url = userUrl)
        .mapNotNull { item ->
            when(item) {
                Result.Loading -> null
                is Result.Error -> throw item.exception
                is Result.Success -> {
                    userDetails = item.data
                    UserDetailsUiState.Success(userDetails = item.data)
                }
            }
        }
        .catch {
            Timber.e(it)
            _action.emit(UserDetailsActions.ShowError(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserDetailsUiState.Loading,
        )
}