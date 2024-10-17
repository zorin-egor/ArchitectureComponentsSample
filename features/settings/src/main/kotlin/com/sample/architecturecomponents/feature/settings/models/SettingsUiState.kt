package com.sample.architecturecomponents.feature.settings.models

import com.sample.architecturecomponents.core.model.SettingsData

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: SettingsData) : SettingsUiState
}