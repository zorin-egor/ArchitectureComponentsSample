package com.sample.architecturecomponent.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentSplashBinding
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import javax.inject.Inject


class SplashFragment : BaseFragment() {

    companion object {
        val TAG = SplashFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }

    private var binding by autoCleared<FragmentSplashBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        ).apply {
            binding = this
            lifecycleOwner = this@SplashFragment
            viewmodel = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        viewModel.navigate.observe(viewLifecycleOwner) {
            navigation.navigate(SplashFragmentDirections.splashToMainScreen())
            requireActivity().finish()
        }
    }

}
