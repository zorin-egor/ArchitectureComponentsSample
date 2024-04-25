package com.sample.architecturecomponents.core.ui.ext

import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

val androidx.compose.ui.graphics.Color.toIntColor: Int
    get() = android.graphics.Color.argb(
        toArgb().alpha,
        toArgb().red,
        toArgb().green,
        toArgb().blue
    )

