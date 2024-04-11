package com.sample.architecturecomponents.core.data.repositories.settings

import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.model.ThemeBrand
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SettingsDataRepositoryImpl @Inject constructor(
    private val dataSourceProto: SettingsDataStoreProto,
) : SettingsDataRepository {

    override val settingsData: Flow<SettingsData> =
        dataSourceProto.settingsData

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