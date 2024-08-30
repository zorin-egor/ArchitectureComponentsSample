package com.sample.architecturecomponents.feature.settings


sealed interface SettingsActions {
    data class ShowError(val error: String) : SettingsActions
    data class RequestPermission(val permission: String) : SettingsActions
}