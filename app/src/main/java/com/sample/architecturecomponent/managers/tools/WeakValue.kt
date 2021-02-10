package com.sample.architecturecomponent.managers.tools

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
* https://habr.com/ru/post/325966/
* */
class WeakValue<T>(obj: T? = null): ReadWriteProperty<Any?, T?> {

    private var wref : WeakReference<T>?

    init {
        this.wref = obj?.let { WeakReference(it) }
    }

    override fun getValue(thisRef:Any? , property: KProperty<*>): T? {
        return wref?.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        wref = value?.let { WeakReference(it) }
    }
}

fun <T> weak(obj: T? = null) = WeakValue(obj)