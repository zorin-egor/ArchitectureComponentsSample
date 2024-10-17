package com.sample.architecturecomponents.feature.settings.models


sealed interface SettingsActions {
    data object None : SettingsActions
    data class ShowError(val error: String) : SettingsActions
    data class RequestPermission(val permission: String) : SettingsActions
}