package com.sample.architecturecomponent.managers.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.updateMarginsRelative

fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

fun Activity.setFullscreen(isFullscreen: Boolean, @ColorInt barColor: Int = Color.BLACK) {
    val color = if (isFullscreen) 0 else barColor
    setStatusBarColor(color)
    setNavigationBarColor(color)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.setDecorFitsSystemWindows(!isFullscreen)
    } else {
        window?.decorView?.apply {
            systemUiVisibility = if (isFullscreen) {
                systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            } else {
                systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE.inv() and
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv() and
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION.inv()
            }
        }
    }
}

fun Activity.setNoLimits(isNoLimits: Boolean) {
    window.apply {
        if (isNoLimits) {
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}

fun Activity.setStatusBarLight(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val appearance = if (isLight) 0 else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        window?.decorView?.windowInsetsController
            ?.setSystemBarsAppearance(appearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
    } else {
        window?.decorView?.apply {
            systemUiVisibility = if (isLight) {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}

fun Activity.setNavigationBarColor(color: Int) {
    window.navigationBarColor = color
}

fun View.updateMargins(@Px start: Int? = null, @Px top: Int? = null, @Px end: Int? = null, @Px bottom: Int? = null) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        start?.also { updateMarginsRelative(start = it) }
        top?.also { updateMarginsRelative(top = it) }
        end?.also { updateMarginsRelative(end = it) }
        bottom?.also { updateMarginsRelative(bottom = it) }
    }
}

fun Context.showBrowser(url: String, title: String? = null) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }, title))
}

internal fun WindowInsets.getBottom(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getInsets(WindowInsets.Type.navigationBars()).bottom
    } else {
        systemWindowInsetBottom
    }
}

internal fun WindowInsets.getTop(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getInsets(WindowInsets.Type.statusBars()).top
    } else {
        systemWindowInsetTop
    }
}