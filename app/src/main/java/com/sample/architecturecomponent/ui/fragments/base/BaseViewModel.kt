package com.sample.architecturecomponent.ui.fragments.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {

    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }

    open val navigate = MutableLiveData<Int?>()

}