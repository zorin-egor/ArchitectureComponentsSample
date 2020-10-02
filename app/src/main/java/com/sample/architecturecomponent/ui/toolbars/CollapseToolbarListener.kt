package com.sample.qr.ui.views.toolbars

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class CollapseToolbarListener(var onCollapseListener: OnCollapseListener) : AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    var state = State.IDLE
        private set

    interface OnCollapseListener {
        fun onToolbarExpand() {}
        fun onToolbarChange(verticalOffset: Int) {}
        fun onToolbarCollapse() {}
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        onCollapseListener?.let {
            when {
                abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    if (state != State.COLLAPSED) {
                        it.onToolbarCollapse()
                        state = State.COLLAPSED
                    }
                }

                verticalOffset == 0 -> {
                    if (state != State.EXPANDED) {
                        it.onToolbarExpand()
                        state = State.EXPANDED
                    }
                }

                else -> {
                    if (state != State.IDLE) {
                        it.onToolbarChange(verticalOffset)
                        state = State.IDLE
                    }
                }
            }
        }
    }
}
