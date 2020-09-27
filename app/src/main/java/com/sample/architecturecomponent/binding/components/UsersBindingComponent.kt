package com.sample.architecturecomponent.binding.components

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import com.sample.architecturecomponent.binding.adapters.CommonAdapters
import com.sample.architecturecomponent.binding.adapters.UsersAdapters


class UsersBindingComponent(fragment: Fragment) : DataBindingComponent {

    private val commonAdapters = CommonAdapters(fragment)

    private val usersAdapters = UsersAdapters()

    override fun getUsersAdapters() = usersAdapters

    override fun getCommonAdapters() = commonAdapters

}
