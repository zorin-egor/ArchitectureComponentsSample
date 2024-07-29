package com.sample.architecturecomponents.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
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
    }

    fun clearCache() {
        settingsRepository.clearCache()
    }

}


