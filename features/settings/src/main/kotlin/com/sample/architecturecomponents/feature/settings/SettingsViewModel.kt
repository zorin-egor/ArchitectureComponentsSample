package com.sample.architecturecomponents.feature.settings

import androidx.lifecycle.ViewModel
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

}

data class UserEditableSettings(
    val brand: String
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
