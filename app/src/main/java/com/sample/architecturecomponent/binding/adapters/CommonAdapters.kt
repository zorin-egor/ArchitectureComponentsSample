package com.sample.architecturecomponent.binding.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener


class CommonAdapters(val fragment: Fragment) {

    @BindingAdapter(value = ["app:imageUrl", "app:imageRequestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment).load(url).listener(listener).into(imageView)
    }

    @BindingAdapter(value = ["app:swipeRefreshing"])
    fun bindSwipeRefresh(swipeView: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipeView.isRefreshing = isRefreshing
    }

    @BindingAdapter(value = ["app:onLongClick"])
    fun bindLongClick(view: View, listener: (View) -> Boolean) {
        view.setOnLongClickListener(listener)
    }

}