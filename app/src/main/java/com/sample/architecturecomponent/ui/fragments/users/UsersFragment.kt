package com.sample.architecturecomponent.ui.fragments.users

import android.os.Bundle
import android.transition.TransitionManager
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.google.android.material.snackbar.Snackbar
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.adapters.BindingComponent
import com.sample.architecturecomponent.databinding.FragmentUsersBinding
import com.sample.architecturecomponent.managers.extensions.getBottom
import com.sample.architecturecomponent.managers.extensions.getTop
import com.sample.architecturecomponent.managers.extensions.updateMargins
import com.sample.architecturecomponent.managers.tools.ExecutorsTool
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.models.User
import com.sample.architecturecomponent.ui.fragments.base.*
import com.sample.architecturecomponent.ui.toolbars.CollapseToolbarListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var executors: ExecutorsTool

    private val bindingComponent: DataBindingComponent by lazy {
        BindingComponent(this)
    }

    private val viewModel: UsersViewModel by viewModels {
        viewModelFactory
    }

    private val collapseToolbarListener: CollapseToolbarListener by lazy {
        CollapseToolbarListener(
            onCollapsed = {
                TransitionManager.beginDelayedTransition(collapsingToolbar)
                collapsingToolbarText.isVisible = true
                collapsingImage.isVisible = false
            },
            onExpanded = {
                TransitionManager.beginDelayedTransition(collapsingToolbar)
                collapsingToolbarText.isVisible = false
                collapsingImage.isVisible = true
            }
        )
    }

    private val isToolbarTextVisible: Boolean
        get() = collapseToolbarListener.state == CollapseToolbarListener.State.IDLE ||
                collapseToolbarListener.state == CollapseToolbarListener.State.COLLAPSED

    private val isToolbarCollapsedState: Boolean
        get() = collapseToolbarListener.state == CollapseToolbarListener.State.COLLAPSED

    private var adapter by autoCleared<UsersAdapter>()
    private var clickedView by autoCleared<View>()
    private var skeleton by autoCleared<RecyclerViewSkeletonScreen>()

    override val layoutId: Int = R.layout.fragment_users

    override val dataBindingComponent: DataBindingComponent = bindingComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun onInsets(view: View, insets: WindowInsets) {
        super.onInsets(view, insets)
        progressBar.updateMargins(bottom = insets.getBottom())
        collapsingToolbar.updateMargins(top = insets.getTop())
        recyclerView.updatePadding(bottom = insets.getBottom())
        collapsingContentLayout.updatePadding(
            top = (insets.getTop() * 1.5).toInt(),
            bottom = (insets.getBottom() * 0.5).toInt()
        )
    }

    private fun init(savedInstanceState: Bundle?) {
        applyBinding<FragmentUsersBinding> { viewmodel = this@UsersFragment.viewModel }
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        recyclerView.apply {
            addOnScrollListener(object : BaseEndListener() {
                override fun onListEnd() {
                    viewModel.next()
                }
            })
            layoutManager = LinearLayoutManager(context)
            adapter = UsersAdapter(
                executors,
                bindingComponent
            ).apply {
                this@UsersFragment.adapter = this
                onLongClickListener = viewModel::userLongClick
                onClickListener = { index, item, view ->
                    clickedView = view
                    viewModel.userClick(index, item)
                }
            }
        }

        postponeEnterTransition()
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }

        appBarLayout.addOnOffsetChangedListener(collapseToolbarListener)
        collapsingToolbarText.isVisible = isToolbarTextVisible
        collapsingImage.isVisible = !isToolbarTextVisible

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        initLiveData()
    }

    private fun initLiveData() {
        viewModel.navigate.observe(viewLifecycleOwner) {
            if (it.arg is User) {
                val extras = FragmentNavigatorExtras(
                    clickedView.userImageView to it.arg.userId.toString()
                )
                navigation.navigate(
                    UsersFragmentDirections.usersToDetailsScreen(it.arg),
                    extras
                )
            }
        }

        viewModel.isResult.observe(viewLifecycleOwner) {
            if (it) {
                skeleton.hide()
            } else {
                skeleton = Skeleton.bind(recyclerView)
                    .adapter(adapter)
                    .load(R.layout.item_list_user)
                    .show()
            }
        }

        viewModel.results.observe(viewLifecycleOwner, adapter::submitList)

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

}