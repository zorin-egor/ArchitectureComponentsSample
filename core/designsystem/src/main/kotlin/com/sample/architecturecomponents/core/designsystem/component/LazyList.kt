package com.sample.architecturecomponents.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LazyListState.setEdgeEvents(prefetch: Int = 3, onTopList: ((Int) -> Unit)? = null, onBottomList: (Int) -> Unit): Boolean {
    return remember(this) {
        println("LazyListState.setEdgeEvents() - remember")
        derivedStateOf {
            println("LazyListState.setEdgeEvents() - derivedStateOf")
            val totalItems = layoutInfo.totalItemsCount
            val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
            val firstItem = layoutInfo.visibleItemsInfo.firstOrNull()
            val lastIndex = lastItem?.index ?: 0
            val firstIndex = firstItem?.index ?: 0

            when {
                layoutInfo.totalItemsCount == 0 -> false
                lastIndex + prefetch > totalItems -> {
                    println("LazyListState.setEdgeEvents($totalItems, $prefetch, $lastIndex) - onBottomList")
                    onBottomList(lastIndex)
                    true
                }
                firstIndex - prefetch < 0 -> {
                    println("LazyListState.setEdgeEvents($totalItems, $prefetch, $firstIndex) - onTopList")
                    onTopList?.run {
                        invoke(firstIndex)
                        true
                    } ?: false
                }
                else -> false
            }
        }
    }.value
}

@SuppressLint("ComposableNaming")
@Composable
fun LazyListState.setEdgeEvents(
    debounce: Long = 500L,
    prefetch: Int = 3,
    onTopList: ((Int) -> Unit)? = null,
    onBottomList: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var eventTopJob: Job? = remember { null }
    var eventBottomJob: Job? = remember { null }

    setEdgeEvents(
        prefetch = prefetch,
        onTopList = onTopList@ { index ->
            if (eventTopJob?.isActive == true) return@onTopList
            eventTopJob = coroutineScope.launch {
                onTopList?.invoke(index)
                delay(debounce)
            }
        },
        onBottomList = onBottomList@ { index ->
            if (eventBottomJob?.isActive == true) return@onBottomList
            eventBottomJob = coroutineScope.launch {
                onBottomList(index)
                delay(debounce)
            }
        }
    )
}