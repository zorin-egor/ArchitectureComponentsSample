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
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.adapters.BindingComponent
import com.sample.architecturecomponent.databinding.FragmentDetailsBinding
import com.sample.architecturecomponent.managers.extensions.getBottom
import com.sample.architecturecomponent.managers.extensions.getTop
import com.sample.architecturecomponent.managers.extensions.updateMargins
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import com.sample.architecturecomponent.ui.fragments.base.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.view_details_titles.view.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bindingComponent: DataBindingComponent by lazy {
        BindingComponent(this)
    }

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory
    }

    override val layoutId: Int = R.layout.fragment_details

    override val dataBindingComponent: DataBindingComponent = bindingComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun onInsets(view: View, insets: WindowInsets) {
        super.onInsets(view, insets)
        titlesScroll.updatePadding(bottom = insets.getBottom())
        backButton.updateMargins(top = insets.getTop())
    }

    private fun init(savedInstanceState: Bundle?) {
        applyBinding<FragmentDetailsBinding> { viewmodel = this@DetailsFragment.viewModel }

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        backButton.setOnClickListener {
            navigation.navigateUp()
        }

        viewModel.item = DetailsFragmentArgs.fromBundle(requireArguments()).userItem
        viewModel.navigate.observe(viewLifecycleOwner) {
            if (it.arg is String) {
                navigation.navigate(DetailsFragmentDirections.detailsToOpenUrlScreen(it.arg))
            }
        }
        viewModel.titles.observe(viewLifecycleOwner) {
            addTitleView(it)
        }
        viewModel.clear.observe(viewLifecycleOwner) {
            titlesLayout.removeAllViews()
        }
        viewModel.message.observe(viewLifecycleOwner) {
            when (it) {
                is Message.Text -> {
                    Snackbar.make(requireView(), it.text, Snackbar.LENGTH_SHORT)
                }
                is Message.Action -> {
                    Snackbar.make(requireView(), it.text, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snackbar_action_title, it.action)
                }
            }.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addTitleView(pair: Pair<String, Spanned>) {
        LayoutInflater.from(requireContext()).inflate(R.layout.view_details_titles, null).also { view ->
            view.detailsNameTitle.text = "${pair.first}: "
            view.detailsValueTitle.also { value ->
                value.text = pair.second
                value.movementMethod = LinkMovementMethod.getInstance()
            }
            view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }.also {
            TransitionManager.beginDelayedTransition(titlesLayout)
            titlesLayout.addView(it)
        }
    }

}
