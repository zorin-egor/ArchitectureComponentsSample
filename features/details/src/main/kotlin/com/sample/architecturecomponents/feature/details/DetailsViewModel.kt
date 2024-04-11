package com.sample.architecturecomponents.feature.details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.domain.GetDetailsUseCase
import com.sample.architecturecomponents.feature.details.navigation.DetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import com.sample.architecturecomponents.core.ui.R as CoreUiR

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDetailsUseCase: GetDetailsUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val usersArgs: DetailsArgs = DetailsArgs(savedStateHandle)

    private val userId = usersArgs.userId

    private val userUrl = usersArgs.userUrl

    private val _action = MutableSharedFlow<DetailsActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<DetailsActions> = _action.asSharedFlow()

    private val _state = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)

    val state: StateFlow<DetailsUiState> = getDetailsUseCase(userId = userId, url = userUrl)
        .catch {
            val error = it.message ?: context.getString(CoreUiR.string.error_unknown)
            Timber.e(error)
            _action.emit(DetailsActions.ShowError(error))
        }
        .map { DetailsUiState.Success(details = it) }
        .onEach(_state::emit)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailsUiState.Loading,
        )
}