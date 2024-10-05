package com.sample.architecturecomponents.feature.settings

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import com.sample.architecturecomponents.core.notifications.NotificationActionsRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val notifier: NotificationActionsRes,
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = settingsRepository.settingsData
        .map { SettingsUiState.Success(settings = it) }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    private val _action = MutableSharedFlow<SettingsActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<SettingsActions> = _action.asSharedFlow()

    fun clearCache() {
        settingsRepository.clearCache()
    }

    fun setNotificationEnabled(value: Boolean) {
        settingsRepository.setNotificationEnable(value)
        if (value) {
            postNotification()
        }
    }

    fun postNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                progressNotificationJob?.cancel()
                progressNotificationJob = viewModelScope.launch {
                    if (!randomNotification()) {
                        _action.emit(SettingsActions.RequestPermission(android.Manifest.permission.POST_NOTIFICATIONS))
                    }
                }
        }
    }

    private var progressNotificationJob: Job? = null
    private var notificationIndex = 0

    private fun randomNotification(): Boolean {
        if (notificationIndex > 1) notificationIndex = 0
        return when(notificationIndex++) {
            0 -> notifier.postText(
                header = R.string.feature_settings_notification,
                body = R.string.feature_settings_notification_text,
            )
            1 ->  notifier.postImageText(
                header = R.string.feature_settings_notification,
                body = R.string.feature_settings_notification_text,
                url = "https://avatars.githubusercontent.com/u/13707343?v=4"
            )
            else -> true
        }
    }

}


