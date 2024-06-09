package com.sample.architecturecomponents.core.data.repositories.theme

import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ThemeRepositoryImpl @Inject constructor(
    private val dataSourceProto: SettingsDataStoreProto,
) : ThemeRepository {

    override val themeData: Flow<ThemeData> =
        dataSourceProto.themeData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        dataSourceProto.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataSourceProto.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        dataSourceProto.setDynamicColorPreference(useDynamicColor)
    }

}