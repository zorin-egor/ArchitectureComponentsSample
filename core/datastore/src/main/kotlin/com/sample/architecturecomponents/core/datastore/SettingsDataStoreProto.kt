package com.sample.architecturecomponents.core.datastore

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsProto
import com.sample.architecturecomponents.core.model.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreProto {

    val settings: Flow<SettingsProto>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setNotificationEnabled(value: Boolean)

}