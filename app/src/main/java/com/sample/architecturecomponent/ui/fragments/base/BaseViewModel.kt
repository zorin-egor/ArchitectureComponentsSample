package com.sample.architecturecomponent.ui.fragments.base

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import com.sample.architecturecomponent.models.ErrorType

data class Screen(val id: Int? = null, val arg: Any? = null)

sealed class Message {
    class Text(val text: CharSequence, vararg args: String) : Message()
    class Action(val text: CharSequence, val action: View.OnClickListener) : Message()
}

abstract class BaseViewModel(private val context: Context) : ViewModel() {

    protected val _navigate = SingleLiveEvent<Screen>()
    protected val _message = SingleLiveEvent<Message>()

    val navigate: LiveData<Screen> = _navigate
    val message: LiveData<Message> = _message

    protected fun handleError(value: ErrorType) {
        when (value) {
            is ErrorType.Error -> showMessage(R.string.error_message, value.message)
            is ErrorType.IOConnection -> showMessage(R.string.error_io)
            is ErrorType.UnknownHost -> showMessage(R.string.error_host)
            is ErrorType.Connection -> showMessage(R.string.error_network_connection)
            is ErrorType.Unknown -> showMessage(R.string.error_unknown)
            is ErrorType.Unhandled -> showMessage(value.error.message
                    ?: context.getString(R.string.error_unknown))
        }
    }

    protected fun showMessage(message: String) {
        _message.value = Message.Text(message)
    }

    protected fun showMessage(@StringRes id: Int, vararg args: String) {
        showMessage(context.getString(id, *args))
    }

}