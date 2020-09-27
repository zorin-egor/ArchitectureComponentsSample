package com.sample.architecturecomponent.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.components.UsersBindingComponent
import com.sample.architecturecomponent.databinding.FragmentUsersBinding
import com.sample.architecturecomponent.ui.adapters.UsersAdapter
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
                mViewModel.next()
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

    private val mViewModel: UsersViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: UsersAdapter
    private var skeleton: RecyclerViewSkeletonScreen? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentUsersBinding>(
            inflater,
            R.layout.fragment_users,
            container,
            false,
            bindingComponent
        ).apply {
            lifecycleOwner = this@UsersFragment
            viewmodel = mViewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        recyclerView.apply {
            adapter = UsersAdapter(appExecutors, bindingComponent).also { this@UsersFragment.adapter = it }
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(OnEndScroll())
        }

        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }

        mViewModel.isResult.observe(viewLifecycleOwner, Observer {
            if (it) {
                skeleton?.hide()
            } else {
                skeleton = Skeleton.bind(recyclerView)
                    .adapter(adapter)
                    .load(R.layout.item_list_user)
                    .show()
            }
        })

        mViewModel.results.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}