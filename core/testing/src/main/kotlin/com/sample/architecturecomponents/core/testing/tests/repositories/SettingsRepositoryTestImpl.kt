package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.model.SettingsPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryTestImpl : SettingsRepository {

    private val data = MutableStateFlow(
        SettingsData(
            cacheSize = "10MB",
            version = "0.0.1",
            preference = SettingsPreference(
                isNotificationEnabled = true
            )
        )
    )

    override val settingsData: Flow<SettingsData> = data.asStateFlow()

    override fun setNotificationEnable(value: Boolean) = data.update {
        it.copy(preference = it.preference.copy(isNotificationEnabled = value))
    }

    override fun clearCache() = data.update { it.copy(cacheSize = "") }

}