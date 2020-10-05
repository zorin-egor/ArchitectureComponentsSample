package com.sample.architecturecomponent.ui.fragments.base

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent

sealed class Navigate {
    object Default : Navigate()
    class Screen<T>(val id: Int? = null, val arg: T? = null): Navigate()
}

sealed class Message {
    class Text(val text: CharSequence) : Message()
    class Action(val text: CharSequence, val action: View.OnClickListener) : Message()
}

abstract class BaseViewModel(private val context: Context) : ViewModel() {

    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }

    open val navigate = SingleLiveEvent<Navigate>()

    open val message = SingleLiveEvent<Message>()

}