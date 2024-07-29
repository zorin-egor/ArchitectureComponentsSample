package com.sample.architecturecomponents.core.data.repositories.settings

import com.sample.architecturecomponents.core.model.SettingsData
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settingsData: Flow<SettingsData>

    fun setNotificationEnable(value: Boolean)

    fun clearCache()

    suspend fun refresh()

}