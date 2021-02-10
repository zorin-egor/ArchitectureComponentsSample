package com.sample.architecturecomponent.ui.toolbars

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class CollapseToolbarListener(
    private var onCollapsed: (() -> Unit)? = null,
    private var onExpanded: (() -> Unit)? = null,
    private var onIdle: ((Int) -> Unit)? = null,
) : AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    var state = State.IDLE
        private set

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                if (state != State.COLLAPSED) {
                    onCollapsed?.invoke()
                    state = State.COLLAPSED
                }
            }
            verticalOffset == 0 -> {
                if (state != State.EXPANDED) {
                    onExpanded?.invoke()
                    state = State.EXPANDED
                }
            }
            else -> {
                if (state != State.IDLE) {
                    onIdle?.invoke(verticalOffset)
                    state = State.IDLE
                }
            }
        }
    }
}
