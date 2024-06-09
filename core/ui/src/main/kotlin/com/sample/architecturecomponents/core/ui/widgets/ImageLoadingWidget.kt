package com.sample.architecturecomponents.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

@Composable
fun ImageLoadingWidget(
    painter: Painter,
    isLoading: Boolean,
    isError: Boolean,
    placeHolder: ImageVector,
    contentScale: ContentScale,
    modifier: Modifier = Modifier,
    placeholderColorFilter: ColorFilter? = null,
) {
    Box(modifier = modifier) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.Center)
            )
            isError -> Image(
                imageVector = placeHolder,
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = placeholderColorFilter,
                modifier = Modifier.fillMaxSize().align(Alignment.Center)
            )
        }

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
        )
    }
}