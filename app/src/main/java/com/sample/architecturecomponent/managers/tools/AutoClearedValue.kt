package com.sample.architecturecomponent.managers.tools

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T : Any>(
    private val fragment: Fragment
) : ReadWriteProperty<Fragment, T> {

    private var value: T? = null

    private val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
        it?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                value = null
            }
        })
    }

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Value must not be null")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this@AutoClearedValue.value = value
    }
}

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)