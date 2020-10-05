package com.sample.architecturecomponent.ui.fragments.details

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.Expose
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.api.ApiDatabaseResponse
import com.sample.architecturecomponent.api.ApiErrorResponse
import com.sample.architecturecomponent.api.ApiSuccessResponse
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import com.sample.architecturecomponent.model.Details
import com.sample.architecturecomponent.model.User
import com.sample.architecturecomponent.repository.DetailsRepository
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Message
import com.sample.architecturecomponent.ui.fragments.base.Navigate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import java.lang.reflect.Field
import javax.inject.Inject


class DetailsViewModel @Inject constructor(
    val context: Context,
    val detailsRepository: DetailsRepository
) : BaseViewModel(context) {

    companion object {
        val TAG = DetailsViewModel::class.java.simpleName
    }

    var item: User? = null
        set(value) {
            field = value
            value?.also(::getDetails)
        }

    val titles = MutableLiveData<Pair<String, Spanned>>()

    val isSwipe = MutableLiveData<Boolean>()

    val clear = SingleLiveEvent<Void>()

    private var detailsJob: Job? = null
    private var handleJob: Job? = null

    fun refresh() {
        item = item
    }

    private fun getDetails(item: User) {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch {
            detailsRepository.getDetails(item)
                .onStart {
                    isSwipe.value = true
                }
                .collect {
                    isSwipe.value = false
                    when (it) {
                        is ApiSuccessResponse -> {
                            handleItem(item, it.value)
                        }
                        is ApiDatabaseResponse -> {
                            message.value = Message.Text(context.getString(R.string.error_cache))
                            handleItem(item, it.value)
                        }
                        is ApiErrorResponse -> {
                            handleItem(User(0), Details(0))
                            message.value = Message.Text(
                                it.error ?: context.getString(R.string.error_unknown)
                            )
                        }
                    }
                }
        }
    }

    private fun handleItem(user: User, details: Details) {
        handleJob?.cancel()
        handleJob = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                clear.call()
            }
            getClassFields(user, user.javaClass)
            getClassFields(details, details.javaClass.superclass as Class<Details>)
            getClassFields(details, details.javaClass)
        }
    }

    private suspend fun <T> getClassFields(item: Any, clazz: Class<in T>) {
        clazz.declaredFields.asSequence().filter(::fieldFilter).forEach { field ->
            field.isAccessible = true

            if (field.annotations.any { it.annotationClass == Expose::class }) {
                val title = field.name ?: "-"
                val value = field.get(item) as? String ?: "-"
                val spanned = SpannableStringBuilder(value).apply {
                    if (URLUtil.isNetworkUrl(value)) {
                        setSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                navigate.value = Navigate.Screen(arg = value)
                            }
                        }, 0, value.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }

                withContext(Dispatchers.Main) {
                    titles.value = Pair(title, spanned)
                }

                delay(100)
            }
        }
    }

    private fun fieldFilter(field: Field): Boolean {
        return when {
            "avatarUrl" == field.name -> false
            "nodeId" == field.name -> false
            else -> true
        }
    }

}