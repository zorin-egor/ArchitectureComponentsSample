package com.sample.architecturecomponent.managers.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.updateMarginsRelative


@RequiresApi(Build.VERSION_CODES.M)
fun Activity.getStatusBarHeightInsets(): Int {
    return window.decorView.rootWindowInsets.systemWindowInsetTop
}

fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

fun Activity.setStatusBarTranslucent(isTranslucent: Boolean) {
    window.apply {
        if (isTranslucent) {
            addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}

fun Activity.setFullscreen(isFullscreen: Boolean) {
    setStatusBarColor(0x00000000)
    setNavigationBarColor(0x00000000)
    window.decorView.apply {
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

fun Activity.setStatusBarNoLimits(isOverscan: Boolean) {
    window.apply {
        if (isOverscan) {
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN)
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

fun Activity.isStatusBarNoLimits(): Boolean {
    return (window.attributes.flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0
}

fun Activity.setStatusBarLight(isLight: Boolean) {
    window.decorView.apply {
        systemUiVisibility = if (isLight) {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.getNavigationBarHeightInsets(): Int {
    return window.decorView.rootWindowInsets.systemWindowInsetBottom
}

fun Activity.setNavigationBarColor(color: Int) {
    window.navigationBarColor = color
}

fun Activity.setNavigationBarColorRes(@ColorRes color: Int) {
    setNavigationBarColor(ContextCompat.getColor(this, color))
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