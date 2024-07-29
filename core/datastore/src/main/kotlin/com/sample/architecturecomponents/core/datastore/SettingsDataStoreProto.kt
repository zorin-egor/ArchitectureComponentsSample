package com.sample.architecturecomponents.core.datastore

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsPreferenceData
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreProto {

    val themeData: Flow<ThemeData>

    val settingsData: Flow<SettingsPreferenceData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setNotificationEnabled(value: Boolean)

}