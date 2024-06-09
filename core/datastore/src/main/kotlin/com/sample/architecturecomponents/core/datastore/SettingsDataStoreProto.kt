package com.sample.architecturecomponents.core.datastore

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreProto {

    val themeData: Flow<ThemeData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}