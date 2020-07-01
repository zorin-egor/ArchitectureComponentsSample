package com.sample.architecturecomponent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.FragmentDetailsBinding
import com.sample.architecturecomponent.viewmodels.DetailsViewModel

class DetailsFragment : BaseFragment() {

    companion object {
        val TAG = DetailsFragment::class.java.simpleName
    }

    private val mViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentDetailsBinding>(inflater, R.layout.fragment_details, container, false).apply {
            lifecycleOwner = this@DetailsFragment
            viewmodel = mViewModel
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
