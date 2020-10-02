package com.sample.architecturecomponent.ui.activities

import android.os.Bundle
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.setFullscreen
import com.sample.architecturecomponent.managers.extensions.setStatusBarLight

class MainActivity : BaseActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarLight(true)
        setFullscreen(true)
    }

}
