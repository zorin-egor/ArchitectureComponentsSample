package com.sample.architecturecomponent.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    companion object {
        private const val UNDEFINED_VALUE = -1
    }

    protected open val layoutId: Int = UNDEFINED_VALUE

    protected var viewBind: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = DataBindingUtil.setContentView(this, layoutId)
    }

}