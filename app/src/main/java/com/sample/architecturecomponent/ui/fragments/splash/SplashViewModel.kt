package com.sample.architecturecomponent.ui.fragments.splash

import android.content.Context
import androidx.lifecycle.liveData
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Navigate
import kotlinx.coroutines.delay
import javax.inject.Inject


class SplashViewModel @Inject constructor(
   val context: Context
) : BaseViewModel(context) {

    companion object {
        val TAG = SplashViewModel::class.java.simpleName

        private val TIMER_MAX = 1000
        private val TIMER_DELAY = 100
    }

    var progress = loading()

    private fun loading() = liveData {
        repeat(TIMER_MAX / TIMER_DELAY + 1) {
            emit(Pair(it * TIMER_DELAY, TIMER_MAX))
            delay(TIMER_DELAY.toLong())
        }
        navigate.value = Navigate.Default
    }

}