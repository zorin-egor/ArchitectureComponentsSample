package com.sample.architecturecomponents.core.ui.ext

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink

fun Context.openBrowser(uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor).build()

    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()

    customTabsIntent.launchUrl(this, uri)
}

fun getHyperLink(url: String, title: String? = null): AnnotatedString {
    return buildAnnotatedString {
        withLink(link = LinkAnnotation.Url(
            url = url,
            style = SpanStyle(color = Color.Blue)
        )) {
            append(title ?: url)
        }
    }
}