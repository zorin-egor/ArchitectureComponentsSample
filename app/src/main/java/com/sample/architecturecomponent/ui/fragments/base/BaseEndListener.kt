package com.sample.architecturecomponent.ui.fragments.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BaseEndListener(private val prefetchIndex: Int = PREFETCH_INDEX) : RecyclerView.OnScrollListener() {

    companion object {
        private const val PREFETCH_INDEX = 5
    }

    private var isDown = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        (recyclerView.layoutManager as? LinearLayoutManager)?.let { manager ->
            val visibleItemCount = manager.childCount
            val totalItemCount = manager.itemCount
            val firstVisibleItemPosition = manager.findFirstVisibleItemPosition()
            if (isDown && visibleItemCount + firstVisibleItemPosition >= totalItemCount - prefetchIndex) {
                onListEnd()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        isDown = dy > 0
    }

    abstract fun onListEnd()

}