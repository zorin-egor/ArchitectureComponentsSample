package com.sample.architecturecomponent.ui.fragments.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentSplashBinding
import com.sample.architecturecomponent.managers.extensions.flowLifecycle
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        viewBind.viewmodel = this@SplashFragment.viewModel
        viewModel.navigate.flowLifecycle(viewLifecycleOwner) {
            navigator.navigate(SplashFragmentDirections.splashToMainScreen())
            requireActivity().finish()
        }
    }

}
