package com.sample.architecturecomponents.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data object Empty : UiState<Nothing>
    open class Error(val throwable: Throwable? = null) : UiState<Nothing>
    data class Success<T>(val item: T) : UiState<T>
}

abstract class StateViewModel<State, Action, Event>(
    private val initialAction: Action
) : ViewModel() {

    abstract val state: StateFlow<State>

    protected val replyCacheState get() = state.replayCache

    protected val lastItemCacheState get() = replyCacheState.lastOrNull()

    protected val isCacheStateEmpty get() = replyCacheState.isEmpty()

    private val _action = MutableStateFlow(initialAction)

    val action: StateFlow<Action> = _action.asStateFlow()

    protected fun setAction(item: Action) {
        _action.tryEmit(item)
    }

    abstract fun setEvent(item: Event)

}

abstract class UiStateViewModel<StateMode, Action, Event>(
    initialAction: Action
): StateViewModel<UiState<StateMode>, Action, Event>(
    initialAction = initialAction
) {

    protected suspend inline fun <reified T> FlowCollector<UiState<T>>.updateSuccessState(action: (T) -> T) {
        val lastItemInCache = (lastItemCacheState as? UiState.Success<*>)?.item
        if (lastItemInCache is T) {
            emit(UiState.Success(action(lastItemInCache)))
        }
    }

    protected inline fun <reified T> getLastSuccessStateOrNull(): T? = replyCacheState.lastOrNull { it is UiState.Success<*> }
        ?.let { it as? UiState.Success<*> }
        ?.let { it.item as? T }
}




