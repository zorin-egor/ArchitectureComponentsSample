package com.sample.architecturecomponent.binding.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.plus
import com.sample.architecturecomponent.managers.extensions.toSpanned
import com.sample.architecturecomponent.models.User

class BindingAdapters(val fragment: Fragment) {

    @BindingAdapter(value = ["app:imageUrl", "app:imageRequestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment)
            .load(url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    ((target as? DrawableImageViewTarget)?.view as? AppCompatImageView)?.apply {
                        scaleType = ImageView.ScaleType.CENTER
                        imageTintList = ColorStateList.valueOf(Color.RED)
                    }
                    listener?.onLoadFailed(e, model, target, isFirstResource)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    ((target as? DrawableImageViewTarget)?.view as? AppCompatImageView)?.apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        imageTintList = null
                    }
                    listener?.onResourceReady(resource, model, target, dataSource, isFirstResource)
                    return false
                }
            })
            .error(R.drawable.ic_baseline_cloud_off_24)
            .into(imageView)
    }

    @BindingAdapter(value = ["app:swipeRefreshing"])
    fun bindSwipeRefresh(swipeView: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipeView.isRefreshing = isRefreshing
    }

    @BindingAdapter(value = ["app:onLongClick"])
    fun bindLongClick(view: View, listener: (View) -> Boolean) {
        view.setOnLongClickListener(listener)
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter(value = ["app:userTitle"])
    fun bindUserTitle(textView: TextView, item: User?) {
        textView.text = (item?.login ?: "-").plus( ": ").toSpanned(textView.context, R.color.colorPrimaryDark, Typeface.BOLD) +
                item?.userId.toString().toSpanned(textView.context, R.color.colorPrimary, Typeface.ITALIC)
    }

}