package com.sample.architecturecomponent.ui.fragments.base

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


abstract class BaseViewModel(private val context: Context) : ViewModel() {

    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }

    open val navigate = SingleLiveEvent<Any>()

    open val message = SingleLiveEvent<Pair<CharSequence, View.OnClickListener?>>()

    protected suspend fun handleError(error: ResponseBody?) {
        withContext(Dispatchers.Main) {
            message.value = Pair(error?.string() ?: context.getString(R.string.error_unknown), null)
        }
    }

}