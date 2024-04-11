package com.sample.architecturecomponents.core.datastore

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.model.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreProto {

    val settingsData: Flow<SettingsData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}