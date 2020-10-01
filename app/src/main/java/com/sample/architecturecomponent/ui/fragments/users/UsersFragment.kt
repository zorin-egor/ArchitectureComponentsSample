package com.sample.architecturecomponent.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.google.android.material.snackbar.Snackbar
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.components.UsersBindingComponent
import com.sample.architecturecomponent.databinding.FragmentUsersBinding
import com.sample.architecturecomponent.managers.tools.autoCleared
import com.sample.architecturecomponent.model.UserItem
import com.sample.architecturecomponent.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject


class UsersFragment : BaseFragment() {

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
        UsersBindingComponent(this)
    }

    private val viewModel: UsersViewModel by viewModels {
        viewModelFactory
    }

    private var binding by autoCleared<FragmentUsersBinding>()
    private var adapter by autoCleared<UsersAdapter>()
    private var skeleton: RecyclerViewSkeletonScreen? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentUsersBinding>(
            inflater,
            R.layout.fragment_users,
            container,
            false,
            bindingComponent
        ).apply {
            binding = this
            lifecycleOwner = this@UsersFragment
            viewmodel = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        recyclerView.apply {
            addOnScrollListener(OnEndScroll())
            layoutManager = LinearLayoutManager(context)
            adapter = UsersAdapter(
                appExecutors,
                bindingComponent
            ).apply {
                this@UsersFragment.adapter = this
                onClickListener = viewModel::userClick
                onLongClickListener = viewModel::userLongClick
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.navigate.observe(viewLifecycleOwner) {
            (it as? UserItem)?.also { item ->
                navigation.navigate(UsersFragmentDirections.usersToDetailsScreen(item))
            }
        }

        viewModel.isResult.observe(viewLifecycleOwner) {
            if (it) {
                skeleton?.hide()
            } else {
                skeleton = Skeleton.bind(recyclerView)
                    .adapter(adapter)
                    .load(R.layout.item_list_user)
                    .show()
            }
        }

        viewModel.results.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.message.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it.first, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snackbar_action_title, it.second)
                .show()
        }
    }

}