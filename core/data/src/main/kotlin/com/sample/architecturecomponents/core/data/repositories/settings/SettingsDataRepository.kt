package com.sample.architecturecomponents.core.data.repositories.settings

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.model.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsDataRepository {

    val settingsData: Flow<SettingsData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

}
