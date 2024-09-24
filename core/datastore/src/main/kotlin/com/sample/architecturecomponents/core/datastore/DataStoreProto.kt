package com.sample.architecturecomponents.core.datastore

import androidx.datastore.core.DataStore
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.SettingsPreference
import com.sample.architecturecomponents.core.model.SettingsProto
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreProto @Inject constructor(
    private val dataStore: DataStore<SettingsDataStore>,
): SettingsDataStoreProto {

    override val settings = dataStore.data
        .map {
            SettingsProto(
                themeData = ThemeData(
                    themeBrand = when (it.themeBrand) {
                        null,
                        ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                        ThemeBrandProto.UNRECOGNIZED,
                        ThemeBrandProto.THEME_BRAND_DEFAULT, -> ThemeBrand.DEFAULT
                        ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
                    },
                    darkThemeConfig = when (it.darkThemeConfig) {
                        null,
                        DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                        DarkThemeConfigProto.UNRECOGNIZED,
                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM, -> DarkThemeConfig.FOLLOW_SYSTEM
                        DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
                        DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                    },
                    useDynamicColor = it.useDynamicColor,
                ),
                settingsPreference = SettingsPreference(
                    isNotificationEnabled = it.notificationEnabled
                )
            )
        }

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        dataStore.updateData {
            it.copy {
                this.themeBrand = when (themeBrand) {
                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
                }
            }
        }
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        dataStore.updateData {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }

    override suspend fun setNotificationEnabled(value: Boolean) {
        dataStore.updateData {
            it.copy {
                notificationEnabled = value
            }
        }
    }
}