package com.sample.architecturecomponent.ui.fragments.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController

abstract class BaseDialogFragment : DialogFragment() {

    companion object {
        private val TAG = BaseDialogFragment::class.java.simpleName
    }

    protected val navigate: NavController
        get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (onBackPressed()) {
                if (!navigate.navigateUp()) {
                    requireActivity().finish()
                }
            }
        }

        navigate.addOnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChange(destination)
        }
    }

    protected open fun onBackPressed(): Boolean {
        return true
    }

    protected open fun onDestinationChange(navDestination: NavDestination) {
        Log.d(TAG, "onDestinationChange($navDestination)")
    }

}
