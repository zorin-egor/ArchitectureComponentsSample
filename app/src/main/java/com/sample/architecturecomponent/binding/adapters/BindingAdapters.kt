package com.sample.architecturecomponent.binding.adapters

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.plus
import com.sample.architecturecomponent.managers.extensions.toSpanned
import com.sample.architecturecomponent.model.UserItem


class BindingAdapters(val fragment: Fragment) {

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

    @BindingAdapter(value = ["app:userTitle"])
    fun bindUserTitle(textView: TextView, item: UserItem?) {
        textView.text = (item?.login ?: "-").plus( ": ").toSpanned(textView.context, R.color.colorPrimaryDark, Typeface.BOLD) +
                (item?.id ?: "-").toSpanned(textView.context, R.color.colorPrimary, Typeface.ITALIC)
    }

}