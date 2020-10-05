package com.sample.architecturecomponent.ui.fragments.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.sample.architecturecomponent.di.Injectable


abstract class BaseFragment : Fragment(), Injectable {

    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }

    protected val navigation: NavController
        get() = findNavController()

    protected var insets: WindowInsets? = null

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
                if (!navigation.navigateUp()) {
                    requireActivity().finish()
                }
            }
        }

        navigation.addOnDestinationChangedListener { controller, destination, arguments ->
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
        this@BaseFragment.insets = insets
    }
}
