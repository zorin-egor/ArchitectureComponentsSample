package com.sample.architecturecomponent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentSplashBinding
import com.sample.architecturecomponent.viewmodels.SplashViewModel


class SplashFragment : BaseFragment() {

    companion object {
        val TAG = SplashFragment::class.java.simpleName
    }

    private val mViewModel: SplashViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentSplashBinding>(inflater, R.layout.fragment_splash, container, false).apply {
            lifecycleOwner = this@SplashFragment
            viewmodel = mViewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel.navigate.observe(viewLifecycleOwner, Observer<Int?> {
            fragmentHost.navigate(R.id.splash_to_main_action_screen)
            requireActivity().finish()
        })
    }

}
