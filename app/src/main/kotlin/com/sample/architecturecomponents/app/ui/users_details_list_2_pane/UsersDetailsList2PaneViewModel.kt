package com.sample.architecturecomponents.app.ui.users_details_list_2_pane

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val USER_DETAILS_LIST_2_PANE_ARGS = "user_details_list_2_pane_args"

@HiltViewModel
class UsersDetailsList2PaneViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val selectedUser: StateFlow<UserDetailsList2PaneArgs?> = savedStateHandle.getStateFlow(USER_DETAILS_LIST_2_PANE_ARGS, null)
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null
        )

    fun onUserClick(userId: Long, userUrl: String) {
        savedStateHandle[USER_DETAILS_LIST_2_PANE_ARGS] = UserDetailsList2PaneArgs(
            userId = userId,
            userUrl = userUrl
        )
    }
}

