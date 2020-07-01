package com.sample.architecturecomponent.viewmodels

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class SplashViewModel() : BaseViewModel() {

    companion object {
        val TAG = SplashViewModel::class.java.simpleName

        private val TIMER_MAX = 3000
        private val TIMER_DELAY = 100
    }

    var progress = loading()

    private fun loading() = liveData {

        withContext(Dispatchers.Default) {
            repeat(TIMER_MAX / TIMER_DELAY + 1) {
                emit(Pair(it * TIMER_DELAY, TIMER_MAX))
                delay(TIMER_DELAY.toLong())
            }
        }

        navigate.value = null
    }

}