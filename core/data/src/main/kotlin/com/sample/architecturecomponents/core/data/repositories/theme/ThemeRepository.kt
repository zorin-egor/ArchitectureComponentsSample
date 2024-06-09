package com.sample.architecturecomponents.core.data.repositories.theme

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    val themeData: Flow<ThemeData>

    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

}
