package com.sample.architecturecomponent.ui.fragments.base

import android.content.Context
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "$this-onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$this-onCreate($savedInstanceState)")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "$this-onViewStateRestored($savedInstanceState)")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "$this-onStart()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "$this-onCreateView($savedInstanceState)")
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
        Log.d(TAG, "$this-onViewCreated($savedInstanceState)")
        init(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "$this-onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "$this-onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "$this-onStop()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "$this-onSaveInstanceState($outState)")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "$this-onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "$this-onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "$this-onDetach()")
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
