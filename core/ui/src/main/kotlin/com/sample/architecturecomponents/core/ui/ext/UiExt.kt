package com.sample.architecturecomponents.core.ui.ext

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val windowSizeClass: WindowSizeClass
    @Composable get() {
        val config = LocalConfiguration.current
        return WindowSizeClass.calculateFromSize(DpSize(width = config.screenWidthDp.dp, height = config.screenHeightDp.dp))
    }

val shouldShowBottomBar: Boolean
    @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

val shouldShowNavRail: Boolean
    @Composable get() = !shouldShowBottomBar

val localViewVMStoreOwner: ViewModelStoreOwner?
    @Composable get() = LocalView.current.findViewTreeViewModelStoreOwner()

val rememberLocalViewVMStoreOwner: ViewModelStoreOwner?
    @Composable get() {
        val localView = LocalView.current
        return remember(key1 = localView.id) {
            localView.findViewTreeViewModelStoreOwner()
        }
    }

val rememberActivityVMStoreOwner: ViewModelStoreOwner?
    @Composable get() {
        val localContext = LocalContext.current
        return remember(key1 = localContext.toString()) {
            localContext.findViewModelStoreOwner()
        }
    }

@Composable
inline fun <reified VM : ViewModel> rootViewModel(): VM? =
    rememberActivityVMStoreOwner?.let { viewModel<VM>(viewModelStoreOwner = it) }