package com.sample.architecturecomponent.ui.fragments.users

import android.os.Bundle
import android.transition.TransitionManager
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.google.android.material.snackbar.Snackbar
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.adapters.BindingComponent
import com.sample.architecturecomponent.databinding.FragmentUsersBinding
import com.sample.architecturecomponent.managers.extensions.updateMargins
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.model.User
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import com.sample.architecturecomponent.ui.fragments.base.Message
import com.sample.architecturecomponent.ui.fragments.base.Navigate
import com.sample.qr.ui.views.toolbars.CollapseToolbarListener
import kotlinx.android.synthetic.main.fragment_details.view.*
import javax.inject.Inject


class UsersFragment : BaseFragment(), CollapseToolbarListener.OnCollapseListener {

    companion object {
        val TAG = UsersFragment::class.java.simpleName
    }

    private inner class OnEndScroll(val prefetchIndex: Int = 5) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            if (lastPosition + prefetchIndex >= adapter.itemCount - 1) {
                viewModel.next()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val bindingComponent: DataBindingComponent by lazy {
        BindingComponent(this)
    }

    private val viewModel: UsersViewModel by viewModels {
        viewModelFactory
    }

    private val collapseToolbarListener: CollapseToolbarListener by lazy {
        CollapseToolbarListener(this)
    }

    private var binding by autoCleared<FragmentUsersBinding>()
    private var adapter by autoCleared<UsersAdapter>()
    private var clickedView by autoCleared<View>()
    private var skeleton by autoCleared<RecyclerViewSkeletonScreen>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentUsersBinding>(
            inflater,
            R.layout.fragment_users,
            container,
            false,
            bindingComponent
        ).also { bind ->
            bind.lifecycleOwner = this@UsersFragment
            bind.viewmodel = viewModel
            binding = bind
            sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun onToolbarExpand() {
        TransitionManager.beginDelayedTransition(binding.collapsingToolbar)
        binding.collapsingToolbarText.visibility = View.INVISIBLE
    }

    override fun onToolbarCollapse() {
        TransitionManager.beginDelayedTransition(binding.collapsingToolbar)
        binding.collapsingToolbarText.visibility = View.VISIBLE
    }

    override fun onInsets(view: View, insets: WindowInsets) {
        super.onInsets(view, insets)
        binding.collapsingToolbar.updateMargins(top = insets.systemWindowInsetTop)
        binding.recyclerView.updatePadding(bottom = insets.systemWindowInsetBottom)
        binding.collapsingContentLayout.updatePadding(
            top = (insets.systemWindowInsetTop * 1.5).toInt(),
            bottom = (insets.systemWindowInsetTop * 0.5).toInt()
        )
    }

    private fun init(savedInstanceState: Bundle?) {
        binding.recyclerView.apply {
            addOnScrollListener(OnEndScroll())
            layoutManager = LinearLayoutManager(context)
            adapter = UsersAdapter(
                appExecutors,
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
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }

        // Workaround bug back press
        binding.appBarLayout.apply {
            addOnOffsetChangedListener(collapseToolbarListener)
            viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    collapseToolbarListener.onOffsetChanged(binding.appBarLayout, binding.appBarLayout.height)
                }
            })
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        initLiveData()
    }

    private fun initLiveData() {
        viewModel.navigate.observe(viewLifecycleOwner) {
            when (it) {
                is Navigate.Screen<*> -> {
                    (it.arg as? User)?.also { user ->
                        val extras = FragmentNavigatorExtras(
                            clickedView.userImageView to user.userId.toString()
                        )
                        navigation.navigate(
                            UsersFragmentDirections.usersToDetailsScreen(user),
                            extras
                        )
                    }
                }
            }
        }

        viewModel.isResult.observe(viewLifecycleOwner) {
            if (it) {
                skeleton.hide()
            } else {
                skeleton = Skeleton.bind(binding.recyclerView)
                    .adapter(adapter)
                    .load(R.layout.item_list_user)
                    .show()
            }
        }

        viewModel.results.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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
            }.apply {
//                view.updateMargins(bottom = insets?.systemWindowInsetBottom)

                view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        view.layoutParams.height = view.height + (insets?.systemWindowInsetBottom ?: 0)
                        view.requestLayout()
                    }
                })
            }.show()
        }
    }

}