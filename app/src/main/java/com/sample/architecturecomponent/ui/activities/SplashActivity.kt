package com.sample.architecturecomponent.ui.activities

import android.os.Bundle
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.ActivitySplashBinding
import com.sample.architecturecomponent.managers.extensions.setNoLimits
import com.sample.architecturecomponent.managers.extensions.setStatusBarLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutId = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarLight(false)
        setNoLimits(true)
    }

}
