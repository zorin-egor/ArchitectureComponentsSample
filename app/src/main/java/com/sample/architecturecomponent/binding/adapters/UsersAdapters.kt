package com.sample.architecturecomponent.binding.adapters

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.plus
import com.sample.architecturecomponent.managers.extensions.toSpanned
import com.sample.architecturecomponent.model.UserItem


class UsersAdapters {

    @BindingAdapter(value = ["app:userTitle"])
    fun bindUserTitle(textView: TextView, item: UserItem?) {
        textView.text = (item?.login ?: "-").plus( ": ").toSpanned(textView.context, R.color.colorPrimaryDark, Typeface.BOLD) +
                        (item?.id ?: "-").toSpanned(textView.context, R.color.colorPrimary, Typeface.ITALIC)
    }

}