package com.sample.architecturecomponent.ui.fragments.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    context: Context
) : BaseViewModel(context) {

    companion object {
        private const val TIMER_MAX = 1000
        private const val TIMER_DELAY = 100
    }

    private var _progress = loading()
    val progress: LiveData<Pair<Int, Int>> = _progress

    private fun loading() = liveData {
        repeat(TIMER_MAX / TIMER_DELAY + 1) {
            emit(Pair(it * TIMER_DELAY, TIMER_MAX))
            delay(TIMER_DELAY.toLong())
        }
        _navigate.value = Screen()
    }

}