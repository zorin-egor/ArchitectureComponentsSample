package com.sample.architecturecomponents.feature.themes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.core.data.repositories.theme.ThemeRepository
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import com.sample.architecturecomponents.feature.themes.ThemesUiState.Loading
import com.sample.architecturecomponents.feature.themes.ThemesUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ThemesViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    val themesUiState: StateFlow<ThemesUiState> =
        themeRepository.themeData
            .map { userData ->
                Success(
                    settings = UserEditableThemes(
                        brand = userData.themeBrand,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = Loading,
            )

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            themeRepository.setThemeBrand(themeBrand)
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            themeRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            themeRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
}

data class UserEditableThemes(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface ThemesUiState {
    data object Loading : ThemesUiState
    data class Success(val settings: UserEditableThemes) : ThemesUiState
}
