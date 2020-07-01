package com.sample.architecturecomponent.managers.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlideTool @Inject constructor(private val context: Context) {

    enum class Scale {
        NONE, CIRCLE, CENTER_CROP
    }

    fun load(into: ImageView, url: String, isSkipCache: Boolean = false,
             placeholder: Drawable? = null, error: Drawable? = null, listener: RequestListener<Bitmap>? = null) {
        load(into, url, getOptions(Scale.NONE, isSkipCache, placeholder, error, null), Bitmap::class.java, listener)
    }

    fun load(into: ImageView, bitmap: Bitmap, isSkipCache: Boolean = false) {
        load(into, bitmap, getOptions(Scale.NONE, isSkipCache, null, null, null), Bitmap::class.java, null)
    }

    fun load(into: ImageView, url: String, isSkipCache: Boolean = false, size: Point? = null, listener: RequestListener<Bitmap>? = null) {
        load(into, url, getOptions(Scale.NONE, isSkipCache, null, null, size), Bitmap::class.java, listener)
    }

    fun loadCrop(into: ImageView, url: String, @DrawableRes placeholder: Int, @DrawableRes error: Int, isSkipCache: Boolean = false) {
        load(into, url, getOptions(Scale.CIRCLE, isSkipCache, placeholder, error, null), Bitmap::class.java, null)
    }

    fun setCrop(into: ImageView, @DrawableRes resId: Int) {
        load(into, resId, getOptions(Scale.CIRCLE, true, null, null, null), Bitmap::class.java, null)
    }

    fun loadCrop(into: SimpleTarget<Bitmap>, url: String, isSkipCache: Boolean = false) {
        load(into, url, getOptions(Scale.CIRCLE, isSkipCache, null, null, null), Bitmap::class.java, null)
    }

    fun load(into: SimpleTarget<Bitmap>, url: String, isSkipCache: Boolean = false, listener: RequestListener<Bitmap>? = null) {
        load(into, url, getOptions(Scale.NONE, isSkipCache, null, null, null), Bitmap::class.java, listener)
    }

    fun loadGif(into: SimpleTarget<GifDrawable>, url: String, isSkipCache: Boolean = false, listener: RequestListener<GifDrawable>? = null) {
        load(into, url, getOptions(Scale.NONE, isSkipCache, null, null, null), GifDrawable::class.java, listener)
    }

    fun loadFile(into: SimpleTarget<File>, url: String, isSkipCache: Boolean = false, listener: RequestListener<File>? = null) {
        load(into, url, getOptions(Scale.NONE, isSkipCache, null, null, null), File::class.java, listener)
    }

    fun loadDrawable(into: SimpleTarget<Bitmap>, @DrawableRes resId: Int, isSkipCache: Boolean = false) {
        load(into, resId, getOptions(Scale.NONE, isSkipCache, null, null, null), Bitmap::class.java, null)
    }

    fun loadDrawable(into: ImageView, @DrawableRes resId: Int, isSkipCache: Boolean = false, listener: RequestListener<Bitmap>? = null) {
        load(into, resId, getOptions(Scale.NONE, isSkipCache, null, null, null), Bitmap::class.java, listener)
    }

    fun loadAsset(into: SimpleTarget<Bitmap>, assetPath: String, isSkipCache: Boolean = false) {
        val uri = Uri.parse("file:///android_asset${assetPath}")
        load(into, uri, getOptions(Scale.NONE, isSkipCache, null, null, null), Bitmap::class.java, null)
    }

    fun loadAsset(into: ImageView, assetPath: String, isSkipCache: Boolean = false, listener: RequestListener<Bitmap>? = null, scaleType: Scale = Scale.NONE) {
        val uri = Uri.parse("file:///android_asset${assetPath}")
        load(into, uri, getOptions(scaleType, isSkipCache, null, null, null), Bitmap::class.java, listener)
    }

    fun clear(into: Any) {
        if (into is Target<*>) {
            Glide.with(context).clear(into)
        } else {
            Glide.with(context).clear(into as ImageView)
        }
    }

    /*
    * Base methods
    * */
    private fun <T> load(into: Any, load: Any, options: RequestOptions, tClass: Class<T>, requestListener: RequestListener<T>?) {
        val requestBuilder = Glide.with(context).`as`(tClass).apply {
            load(load)
            apply(options)
            listener(requestListener)
        }

        if (into is Target<*>) {
            requestBuilder.into(into as Target<T>)
        } else {
            requestBuilder.into(into as ImageView)
        }
    }


    private fun getOptions(scaleType: Scale, isSkipCache: Boolean,
                           placeholder: Drawable?, error: Drawable?,
                           size: Point?): RequestOptions {
        return RequestOptions().apply {
            placeholder(placeholder)
            error(error)

            if (isSkipCache) {
                skipMemoryCache(true)
                diskCacheStrategy(DiskCacheStrategy.NONE)
            } else {
                skipMemoryCache(false)
                diskCacheStrategy(DiskCacheStrategy.ALL)
            }

            when(scaleType) {
                Scale.CIRCLE -> circleCrop()
                Scale.CENTER_CROP -> centerCrop()
            }

            if (size != null) {
                override(size.x, size.y)
            }
        }
    }

    private fun getOptions(scaleType: Scale, isSkipCache: Boolean,
                           @DrawableRes placeholder: Int, @DrawableRes error: Int,
                           size: Point?): RequestOptions {
        return getOptions(scaleType, isSkipCache, getDrawable(placeholder), getDrawable(error), size)
    }

    private fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return context.getDrawable(resId)
    }

}