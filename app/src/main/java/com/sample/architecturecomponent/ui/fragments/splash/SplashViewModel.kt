package com.sample.architecturecomponent.ui.fragments.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    context: Context
) : BaseViewModel(context) {

    companion object {
        private const val TIMER_MAX = 3000
        private const val TIMER_DELAY = 100
    }

    private var _progress = MutableStateFlow(Pair(0, 0))
    val progress: LiveData<Pair<Int, Int>> = _progress.asLiveData()

    init {
        loading()
    }

    private fun loading() {
        viewModelScope.launch {
            repeat(TIMER_MAX / TIMER_DELAY + 1) {
                _progress.tryEmit(Pair(it * TIMER_DELAY, TIMER_MAX))
                delay(TIMER_DELAY.toLong())
            }
            showScreen(Screen())
        }
    }

}