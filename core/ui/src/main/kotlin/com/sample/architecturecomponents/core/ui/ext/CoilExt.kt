package com.sample.architecturecomponents.core.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.sample.architecturecomponents.core.ui.transformations.CircleCropTransformation

@Composable
fun BorderCircleImageRequest(
    url: String?,
    radiusFactor: Float = 0.6f,
    strokeColor: Color = Color.Transparent,
    strokeWidth: Dp = 0.dp,
    isError: MutableState<Boolean>,
    isLoading: MutableState<Boolean>
): ImageRequest.Builder =
    ImageRequest.Builder(LocalContext.current)
        .data(url)
        .scale(Scale.FILL)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .transformations(
            CircleCropTransformation(
                radiusFactor = radiusFactor,
                strokeWidth = strokeWidth.value,
                strokeColor = strokeColor.toIntColor
            )
        )
        .listener(
            onStart = { isLoading.value = true },
            onError = { _, _ -> isError.value = true },
            onSuccess = { _, _ ->  isLoading.value = false }
        )

