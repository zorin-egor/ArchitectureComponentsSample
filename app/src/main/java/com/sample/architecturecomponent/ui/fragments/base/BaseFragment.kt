package com.sample.architecturecomponent.ui.fragments.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.activity.addCallback
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.sample.architecturecomponent.managers.tools.autoCleared

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    companion object {
        private val TAG = BaseFragment::class.java.simpleName
        private const val UNDEFINED_VALUE = -1
    }

    protected open val layoutId: Int = UNDEFINED_VALUE

    protected open val dataBindingComponent: DataBindingComponent? = null

    protected var viewBind by autoCleared<T>()

    protected val navigator: NavController
        get() = findNavController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (layoutId != UNDEFINED_VALUE) {
            DataBindingUtil.inflate<T>(
                    inflater,
                    layoutId,
                    container,
                    false,
                    dataBindingComponent
            ).also { bind ->
                bind.lifecycleOwner = this
                viewBind = bind
            }.root
        } else {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        requireView().setOnApplyWindowInsetsListener { view, insets ->
            onInsets(view, insets)
            insets
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (onBackPressed()) {
                if (!navigator.navigateUp()) {
                    requireActivity().finish()
                }
            }
        }

        navigator.addOnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChange(destination)
        }
    }

    protected open fun onBackPressed(): Boolean {
        return true
    }

    protected open fun onDestinationChange(navDestination: NavDestination) {
        Log.d(TAG, "onDestinationChange($navDestination)")
    }

    protected open fun onInsets(view: View, insets: WindowInsets) {
        Log.d(TAG, "onInsets($view, $insets)")
    }
}
