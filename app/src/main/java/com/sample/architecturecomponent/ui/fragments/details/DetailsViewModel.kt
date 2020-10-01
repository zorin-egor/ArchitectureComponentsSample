package com.sample.architecturecomponent.ui.fragments.details

import android.content.Context
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import javax.inject.Inject


class DetailsViewModel @Inject constructor(
    val context: Context
) : BaseViewModel() {

    companion object {
        val TAG = DetailsViewModel::class.java.simpleName
    }

    init {

    }

}