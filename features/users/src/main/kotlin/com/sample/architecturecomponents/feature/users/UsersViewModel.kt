package com.sample.architecturecomponents.feature.users

import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUsersUseCase
import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import com.sample.architecturecomponents.core.ui.viewmodels.BaseViewModel
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.feature.users.models.UsersActions
import com.sample.architecturecomponents.feature.users.models.UsersEvents
import com.sample.architecturecomponents.feature.users.models.UsersUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getSearchContentsUseCase: GetUsersUseCase,
) : BaseViewModel<UiState<UsersUiModel>, UsersActions, UsersEvents>() {

    private val isNextUsers = MutableStateFlow(true)

    override val state: StateFlow<UiState<UsersUiModel>> = isNextUsers.filter { it }
        .flatMapConcat {
            Timber.d("UsersViewModel() - flatMapConcat: $it")
            getSearchContentsUseCase()
        }.mapNotNull { item ->
            Timber.d("UsersViewModel() - mapNotNull: $item")

            when(item) {
                Result.Loading -> getLastSuccessStateOrNull<UsersUiModel>()
                    ?.let { UiState.Success(it.copy(isBottomProgress = true)) }
                    ?: UiState.Loading

                is Result.Error -> {
                    when(item.exception) {
                        EmptyException -> UiState.Empty
                        else -> throw item.exception
                    }
                }

                is Result.Success -> {
                    isNextUsers.update { false }

                    if (item.data.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(UsersUiModel(users = item.data, isBottomProgress = false))
                    }
                }
            }
        }.catch { error ->
            Timber.e(error)
            isNextUsers.update { false }
            setAction(UsersActions.ShowError(error))
            updateSuccessState<UsersUiModel> {
                it.copy(isBottomProgress = false)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading
        )

    override fun setEvent(item: UsersEvents) {
        when(item) {
            UsersEvents.NextUser -> isNextUsers.update { true }

            is UsersEvents.OnUserClick -> setAction(UsersActions.NavigateToDetails(
                id = item.item.id,
                url = item.item.url
            ))

            UsersEvents.None -> setAction(UsersActions.None)
        }
    }

}

