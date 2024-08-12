package com.sample.architecturecomponents.core.ui.ext

import androidx.compose.ui.text.AnnotatedString

fun Any.toAnnotatedString(
    builder: (AnnotatedString.Builder.() -> Unit) = {},
): AnnotatedString? = toString()
    .takeIf { it.isNotEmpty() }
    ?.let {
        AnnotatedString.Builder(text = it)
            .apply(builder)
            .toAnnotatedString()
    }


