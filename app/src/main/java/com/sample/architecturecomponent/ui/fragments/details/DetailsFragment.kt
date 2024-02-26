package com.sample.architecturecomponent.ui.fragments.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.adapters.BindingComponent
import com.sample.architecturecomponent.databinding.FragmentDetailsBinding
import com.sample.architecturecomponent.managers.extensions.flowLifecycle
import com.sample.architecturecomponent.managers.extensions.getBottom
import com.sample.architecturecomponent.managers.extensions.getTop
import com.sample.architecturecomponent.managers.extensions.updateMargins
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import com.sample.architecturecomponent.ui.fragments.base.Message
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    private val bindingComponent: DataBindingComponent by lazy {
        BindingComponent(this)
    }

    private val viewModel: DetailsViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_details

    override val dataBindingComponent: DataBindingComponent = bindingComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun onInsets(view: View, insets: WindowInsets) {
        super.onInsets(view, insets)
        viewBind.titlesScroll.updatePadding(bottom = insets.getBottom())
        viewBind.backButton.updateMargins(top = insets.getTop())
    }

    private fun init(savedInstanceState: Bundle?) {
        initViews()
        initObservers()
    }

    private fun initViews() {
        viewBind.viewmodel = this@DetailsFragment.viewModel
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.move)

        viewBind.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewBind.backButton.setOnClickListener {
            navigator.navigateUp()
        }
    }

    private fun initObservers() {
        viewModel.item = DetailsFragmentArgs.fromBundle(requireArguments()).userItem
        viewModel.navigate.flowLifecycle(viewLifecycleOwner) {
            if (it.arg is String) {
                navigator.navigate(DetailsFragmentDirections.detailsToOpenUrlScreen(it.arg))
            }
        }

        viewModel.titles.flowLifecycle(viewLifecycleOwner, ::addTitleView)
        viewModel.clear.flowLifecycle(viewLifecycleOwner) {
            viewBind.titlesLayout.removeAllViews()
        }
        viewModel.message.flowLifecycle(viewLifecycleOwner) {
            when (it) {
                is Message.Text -> {
                    Snackbar.make(requireView(), it.text, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Message.Action -> {
                    Snackbar.make(requireView(), it.text, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snackbar_action_title, it.action)
                        .show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addTitleView(pair: Pair<String, Spanned>) {
        LayoutInflater.from(requireContext()).inflate(R.layout.view_details_titles, null).also { view ->
            view.findViewById<TextView>(R.id.detailsNameTitle).text = "${pair.first}: "
            view.findViewById<TextView>(R.id.detailsValueTitle).also { value ->
                value.text = pair.second
                value.movementMethod = LinkMovementMethod.getInstance()
            }
            view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }.also {
            TransitionManager.beginDelayedTransition(viewBind.titlesLayout)
            viewBind.titlesLayout.addView(it)
        }
    }

}
