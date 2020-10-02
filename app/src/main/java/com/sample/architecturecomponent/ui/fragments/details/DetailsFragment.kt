package com.sample.architecturecomponent.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.adapters.BindingComponent
import com.sample.architecturecomponent.databinding.FragmentDetailsBinding
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.view_details_titles.view.*
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    companion object {
        val TAG = DetailsFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bindingComponent: DataBindingComponent by lazy {
        BindingComponent(this)
    }

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory
    }

    private var binding by autoCleared<FragmentDetailsBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentDetailsBinding>(
            inflater,
            R.layout.fragment_details,
            container,
            false,
            bindingComponent
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

    override fun onInsets(view: View, insets: WindowInsets) {
        super.onInsets(view, insets)
        titlesScroll.updatePadding(bottom = insets.systemWindowInsetBottom)
    }

    private fun init(savedInstanceState: Bundle?) {
        viewModel.item = DetailsFragmentArgs.fromBundle(requireArguments()).userItem
        viewModel.titles.observe(viewLifecycleOwner) {
            addTitleView(it)
        }
    }

    private fun addTitleView(pair: Pair<String, String>) {
        LayoutInflater.from(requireContext()).inflate(R.layout.view_details_titles, null).also { view ->
            view.detailsNameTitle.text = "${pair.first}: "
            view.detailsValueTitle.text = pair.second
            view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }.also {
            TransitionManager.beginDelayedTransition(titlesLayout)
            titlesLayout.addView(it)
        }
    }

}
