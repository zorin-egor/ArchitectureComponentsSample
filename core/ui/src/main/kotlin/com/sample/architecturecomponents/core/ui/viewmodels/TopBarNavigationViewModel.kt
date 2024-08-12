package com.sample.architecturecomponents.core.ui.viewmodels

import androidx.compose.runtime.compositionLocalOf

val LocalTopBarNavigationState = compositionLocalOf<TopBarNavigationState> { error("Must be provide before use") }

class TopBarNavigationViewModel : ResultViewModel<TopBarNavigationState>()

sealed interface TopBarNavigationState {
    data object None : TopBarNavigationState
    data object Back : TopBarNavigationState
    data object Menu : TopBarNavigationState
    data object Search : TopBarNavigationState
    data object Extra : TopBarNavigationState
}