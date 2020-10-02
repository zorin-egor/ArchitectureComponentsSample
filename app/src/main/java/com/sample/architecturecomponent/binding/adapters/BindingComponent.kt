package com.sample.architecturecomponent.binding.adapters

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment


class BindingComponent(fragment: Fragment) : DataBindingComponent {

    private val bindingAdapters = BindingAdapters(fragment)

    override fun getBindingAdapters() = bindingAdapters

}
