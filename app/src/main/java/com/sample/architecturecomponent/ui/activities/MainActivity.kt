package com.sample.architecturecomponent.ui.activities

import android.os.Bundle
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.ActivityMainBinding
import com.sample.architecturecomponent.managers.extensions.setFullscreen
import com.sample.architecturecomponent.managers.extensions.setStatusBarLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarLight(true)
        setFullscreen(true)
    }

}
