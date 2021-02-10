package com.sample.architecturecomponent.ui.activities

import android.os.Bundle
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.setNoLimits
import com.sample.architecturecomponent.managers.extensions.setStatusBarLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarLight(false)
        setNoLimits(true)
    }

}
