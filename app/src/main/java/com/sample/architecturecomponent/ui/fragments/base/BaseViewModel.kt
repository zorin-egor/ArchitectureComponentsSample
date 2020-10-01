package com.sample.architecturecomponent.ui.fragments.base

import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent


abstract class BaseViewModel : ViewModel() {

    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }

    open val navigate = SingleLiveEvent<Any?>()

}