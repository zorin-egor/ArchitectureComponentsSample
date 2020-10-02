package com.sample.qr.ui.views.toolbars

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class CollapseToolbarEvent(var onCollapseToolbar: OnCollapseToolbar) : AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    protected var mCurrentState = State.IDLE

    interface OnCollapseToolbar {
        fun onToolbarExpand() {}
        fun onToolbarChange(verticalOffset: Int) {}
        fun onToolbarCollapse() {}
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        onCollapseToolbar?.let {
            when {
                abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    if (mCurrentState != State.COLLAPSED) {
                        it.onToolbarCollapse()
                        mCurrentState = State.COLLAPSED
                    }
                }

                verticalOffset == 0 -> {
                    if (mCurrentState != State.EXPANDED) {
                        it.onToolbarExpand()
                        mCurrentState = State.EXPANDED
                    }
                }

                else -> {
                    if (mCurrentState != State.IDLE) {
                        it.onToolbarChange(verticalOffset)
                        mCurrentState = State.IDLE
                    }
                }
            }
        }
    }
}
