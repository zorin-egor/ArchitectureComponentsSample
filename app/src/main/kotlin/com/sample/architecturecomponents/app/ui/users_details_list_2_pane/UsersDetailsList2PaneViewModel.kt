package com.sample.architecturecomponents.app.ui.users_details_list_2_pane

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.feature.user_details.navigation.USER_ID_ARG
import com.sample.architecturecomponents.feature.user_details.navigation.USER_URL_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersDetailsList2PaneViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val selectedUser: StateFlow<UserDetailsList2PaneArgs?> =
        savedStateHandle.getStateFlow<Long?>(USER_ID_ARG, null)
            .filterNotNull()
            .zip(savedStateHandle.getStateFlow<String?>(USER_URL_ARG, null)
                .filterNotNull())
            { id, url ->
                UserDetailsList2PaneArgs(
                    userId = id,
                    userUrl = url
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null
            )

    fun onUserClick(userId: Long, userUrl: String) {
        Timber.d("onUserClick($userId, $userUrl)")
        savedStateHandle[USER_ID_ARG] = userId
        savedStateHandle[USER_URL_ARG] = userUrl
    }
}

