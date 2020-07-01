package com.sample.architecturecomponent.ui.fragments

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
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.binding.FragmentDataBindingComponent
import com.sample.architecturecomponent.databinding.FragmentUsersBinding
import com.sample.architecturecomponent.ui.adapters.UsersAdapter
import com.sample.architecturecomponent.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject


class UsersFragment : BaseFragment() {

    companion object {
        val TAG = UsersFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val mViewModel: UsersViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var mAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentUsersBinding>(inflater, R.layout.fragment_users, container, false).apply {
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
            adapter = UsersAdapter(appExecutors, bindingComponent).also { mAdapter = it }
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == mAdapter.itemCount - 1) {
                        mViewModel.next()
                    }
                }
            })
        }

        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }

        mViewModel.results.observe(viewLifecycleOwner, Observer { items ->
            mAdapter.submitList(items)
        })
    }

}
