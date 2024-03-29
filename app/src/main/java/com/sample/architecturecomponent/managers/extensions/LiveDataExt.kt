package com.sample.architecturecomponent.managers.extensions

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * https://github.com/android/architecture-samples/blob/todo-mvvm-live-kotlin/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/Event.kt
 *
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T?) {

    private val isHandled = AtomicBoolean(false)

    /**
     * Returns the content and prevents its use again.
     */
    fun getContent(): T? {
        return if (isHandled.compareAndSet(false, true)) {
            content
        } else {
            null
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T? = content
}


/**
 *
 * https://github.com/android/architecture-samples/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java
 *
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
open class SingleLiveEvent<T> : MutableLiveData<T>() {

    companion object {
        private val TAG = SingleLiveEvent::class.java.simpleName
    }

    private val isHandled = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) {
            if (isHandled.compareAndSet(false, true)) {
                observer.onChanged(value)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        isHandled.set(false)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}