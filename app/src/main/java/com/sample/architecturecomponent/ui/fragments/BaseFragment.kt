package com.sample.architecturecomponent.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


abstract class BaseFragment : Fragment(), HasAndroidInjector {

    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    protected val fragmentHost by lazy {
        NavHostFragment.findNavController(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun androidInjector() = androidInjector

    private fun init(savedInstanceState: Bundle?) {
        // Stub
    }
}
