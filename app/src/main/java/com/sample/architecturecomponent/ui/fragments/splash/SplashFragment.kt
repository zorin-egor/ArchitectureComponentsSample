package com.sample.architecturecomponent.ui.fragments.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentSplashBinding
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }

    override val layoutId: Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        applyBinding<FragmentSplashBinding> {
            viewmodel = this@SplashFragment.viewModel
        }

        viewModel.navigate.observe(viewLifecycleOwner) {
            navigation.navigate(SplashFragmentDirections.splashToMainScreen())
            requireActivity().finish()
        }
    }

}
