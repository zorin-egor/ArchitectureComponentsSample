package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.data.repositories.theme.ThemeRepository
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.model.ThemeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemeRepositoryTestImpl : ThemeRepository {

    private val data = MutableStateFlow(
        ThemeData(
            themeBrand = ThemeBrand.DEFAULT,
            darkThemeConfig = DarkThemeConfig.LIGHT,
            useDynamicColor = false
        )
    )

    override val themeData: Flow<ThemeData> = data.asStateFlow()

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) = data.update {
        it.copy(themeBrand = themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) = data.update {
        it.copy(darkThemeConfig = darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) = data.update {
        it.copy(useDynamicColor = useDynamicColor)
    }
}