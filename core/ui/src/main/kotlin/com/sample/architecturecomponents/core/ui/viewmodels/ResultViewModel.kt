package com.sample.architecturecomponents.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull

abstract class ResultViewModel<T> : ViewModel() {

    private val _result = MutableSharedFlow<Pair<String, T?>>(replay = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    fun emit(key: String, value: T) {
        _result.tryEmit(key to value)
    }

    fun collect(key: String): Flow<T> =
        _result.asSharedFlow()
            .filter { it.first == key }
            .mapNotNull { it.second }

}