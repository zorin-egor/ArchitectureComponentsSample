package com.sample.architecturecomponents.feature.themes

import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.core.testing.tests.repositories.ThemeRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class ThemesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = ThemeRepositoryTestImpl()

    private lateinit var viewModel: ThemesViewModel

    @Before
    fun setup() {
        viewModel = ThemesViewModel(
            themeRepository = repository
        )
    }

    @Test
    fun loadingSettingsViewModelTest() = runTest {
        assertEquals(ThemesUiState.Loading, viewModel.themesUiState.value)
    }

    @Test
    fun setThemeBrandTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.themesUiState.collect() }

        viewModel.updateThemeBrand(ThemeBrand.ANDROID)

        assertEquals(
            ThemesUiState.Success(
               UserEditableThemes(
                   brand = ThemeBrand.ANDROID,
                   useDynamicColor = false,
                   darkThemeConfig = DarkThemeConfig.LIGHT
               ),
            ),
            viewModel.themesUiState.value,
        )
    }

    @Test
    fun setDarkThemeConfigTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.themesUiState.collect() }

        viewModel.updateDarkThemeConfig(DarkThemeConfig.DARK)

        assertEquals(
            ThemesUiState.Success(
                UserEditableThemes(
                    brand = ThemeBrand.DEFAULT,
                    useDynamicColor = false,
                    darkThemeConfig = DarkThemeConfig.DARK
                ),
            ),
            viewModel.themesUiState.value,
        )
    }

    @Test
    fun setDynamicColorPreferenceTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.themesUiState.collect() }

        viewModel.updateDynamicColorPreference(true)

        assertEquals(
            ThemesUiState.Success(
                UserEditableThemes(
                    brand = ThemeBrand.DEFAULT,
                    useDynamicColor = true,
                    darkThemeConfig = DarkThemeConfig.LIGHT
                ),
            ),
            viewModel.themesUiState.value,
        )
    }


}