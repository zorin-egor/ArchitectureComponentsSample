package com.sample.architecturecomponents.feature.user_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUserDetailsUseCase
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import com.sample.architecturecomponents.core.ui.viewmodels.BaseViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.feature.user_details.models.UserDetailsActions
import com.sample.architecturecomponents.feature.user_details.models.UserDetailsEvent
import com.sample.architecturecomponents.feature.user_details.navigation.UserDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserDetailsUseCase: GetUserDetailsUseCase,
) : BaseViewModel<UiState<UserDetails>, UserDetailsActions, UserDetailsEvent>() {

    private val usersArgs: UserDetailsArgs = UserDetailsArgs(savedStateHandle)

    private val userId = usersArgs.userId

    private val userUrl = usersArgs.userUrl

    var userDetails: UserDetails? = null
        private set

    override fun setEvent(item: UserDetailsEvent) {
        when(item) {
            UserDetailsEvent.None -> setAction(UserDetailsActions.None)
            UserDetailsEvent.NavigationBack -> { /* For analytic */ }
            UserDetailsEvent.ShareProfile -> userDetails?.let { setAction(UserDetailsActions.ShareUrl(it.url)) }
        }
    }

    override val state: StateFlow<UiState<UserDetails>> = getUserDetailsUseCase(userId = userId, url = userUrl)
        .mapNotNull { item ->
            when(item) {
                Result.Loading -> UiState.Loading

                is Result.Error -> {
                    when(item.exception) {
                        EmptyException -> UiState.Empty
                        else -> throw item.exception
                    }
                }

                is Result.Success -> {
                    userDetails = item.data
                    UiState.Success(item.data)
                }
            }
        }.catch { error ->
            Timber.e(error)
            setAction(UserDetailsActions.ShowError(error))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading,
        )
}