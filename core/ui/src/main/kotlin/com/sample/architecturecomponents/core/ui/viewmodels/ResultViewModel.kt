package com.sample.architecturecomponents.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

abstract class ResultViewModel<T> : ViewModel() {

    private val _result = Channel<Pair<String, T?>>()

    fun emit(key: String, value: T) {
        _result.trySend(key to value)
    }

    fun collect(key: String, initialValue: T? = null): Flow<T> =
        _result.receiveAsFlow()
            .filter { it.first == key }
            .mapNotNull { it.second }
}