package com.sample.architecturecomponent.ui.fragments.base

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.models.ErrorType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

data class Screen(val id: Int? = null, val arg: Any? = null)

sealed class Message {
    class Text(val text: CharSequence, vararg args: String) : Message()
    class Action(val text: CharSequence, val action: View.OnClickListener) : Message()
}

abstract class BaseViewModel(private val context: Context) : ViewModel() {

    private val _navigate = Channel<Screen>(Channel.CONFLATED)
    private val _message = Channel<Message>(Channel.CONFLATED)

    val navigate: Flow<Screen> = _navigate.receiveAsFlow()
    val message: Flow<Message> = _message.receiveAsFlow()

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
        _message.trySend(Message.Text(message))
    }

    protected fun showMessage(@StringRes id: Int, vararg args: String) {
        showMessage(context.getString(id, *args))
    }

    protected fun showMessage(item: Message) {
        _message.trySend(item)
    }

    protected fun showScreen(item: Screen) {
        _navigate.trySend(item)
    }

}