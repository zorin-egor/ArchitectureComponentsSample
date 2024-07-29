package com.sample.architecturecomponents.feature.settings


sealed interface SettingsActions {
    data class ShowError(val error: String) : SettingsActions
}