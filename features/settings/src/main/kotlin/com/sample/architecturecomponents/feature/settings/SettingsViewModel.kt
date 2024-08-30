package com.sample.architecturecomponents.feature.settings

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.common.di.MainScreenClass
import com.sample.architecturecomponents.core.common.extensions.getTopIntent
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import com.sample.architecturecomponents.core.notifications.NotificationConfig
import com.sample.architecturecomponents.core.notifications.Notifier
import com.sample.architecturecomponents.core.notifications.removeNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

import com.sample.architecturecomponents.core.ui.R as CoreUiR

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val notifier: Notifier,
    @MainScreenClass private val mainScreenClass: Class<*>
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = settingsRepository.settingsData
        .map { SettingsUiState.Success(settings = it) }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    private val _action = MutableSharedFlow<SettingsActions>(replay = 0, extraBufferCapacity = 1)

    val action: SharedFlow<SettingsActions> = _action.asSharedFlow()

    fun setNotificationEnabled(value: Boolean) {
        settingsRepository.setNotificationEnable(value)
        if (value) {
            postNotification()
        }
    }

    fun clearCache() {
        settingsRepository.clearCache()
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

    private suspend fun randomNotification(): Boolean {
        if (notificationIndex > 3) notificationIndex = 0
        return when(notificationIndex++) {
            0 -> notifier.postText(
                header = context.getString(R.string.feature_settings_notification),
                body = context.getString(R.string.feature_settings_notification_text),
            )
            1 -> notifier.postTextAction(
                header = context.getString(R.string.feature_settings_notification),
                body = context.getString(R.string.feature_settings_notification_text),
                actionIcon = com.sample.architecturecomponents.core.notification.R.drawable.core_notifications_ic_nia_notification,
                actionText = context.getString(CoreUiR.string.cancel),
                actionIntent = context.getTopIntent(mainScreenClass),
            )
            2 ->  notifier.postImageText(
                header = context.getString(R.string.feature_settings_notification),
                body = context.getString(R.string.feature_settings_notification_text),
                url = "https://avatars.githubusercontent.com/u/13707343?v=4"
            )
            3 -> {
                val id = Random.nextInt()
                val config = NotificationConfig(
                    id = id,
                )
                var i = 0
                while (i <= 100) {
                    if (!notifier.postProgress(
                        header = context.getString(R.string.feature_settings_notification),
                        100,
                        progress = i,
                        actionIcon = com.sample.architecturecomponents.core.notification.R.drawable.core_notifications_ic_nia_notification,
                        actionText = context.getString(CoreUiR.string.cancel),
                        actionIntent = context.getTopIntent(mainScreenClass),
                        config = config
                    )) return false

                    i += 10
                    delay(1000)
                }

                context.removeNotification(config.id, config.tag)
                return true
            }
            else -> true
        }
    }

}


