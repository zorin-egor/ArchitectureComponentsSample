package com.sample.architecturecomponents.core.ui.transformations

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class CircleCropTransformation(
    private val radiusFactor: Float = 1.0f,
    private val strokeColor: Int = Color.TRANSPARENT,
    private val strokeWidth: Float = 0.0f,
) : Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val strokePaint = Paint()
        strokePaint.color = strokeColor
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth

        val minSize = minOf(input.width, input.height)
        val minSizeFactor = (minSize * radiusFactor).toInt()
        val radius = minSizeFactor / 2f
        val output = createBitmap(minSizeFactor, minSizeFactor, input.config)

        output.applyCanvas {
            drawCircle(radius, radius, radius, bitmapPaint)
            bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, bitmapPaint)
            drawCircle(radius, radius, radius - strokeWidth / 2, strokePaint)
        }

        return output
    }

    override fun equals(other: Any?) = other is CircleCropTransformation

    override fun hashCode() = javaClass.hashCode()
}