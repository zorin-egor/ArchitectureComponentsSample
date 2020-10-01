package com.sample.architecturecomponent.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentDetailsBinding
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    companion object {
        val TAG = DetailsFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory
    }

    private var binding by autoCleared<FragmentDetailsBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentDetailsBinding>(
            inflater,
            R.layout.fragment_details,
            container,
            false
        ).apply {
            binding = this
            lifecycleOwner = this@DetailsFragment
            viewmodel = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        // Stub
    }

}
