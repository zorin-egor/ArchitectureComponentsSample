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
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.model.DetailsItem
import com.sample.architecturecomponent.model.UserItem
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import kotlinx.coroutines.*
import java.lang.reflect.Field
import javax.inject.Inject


class DetailsViewModel @Inject constructor(
    val context: Context,
    val retrofitTool: RetrofitTool<Api>
) : BaseViewModel(context) {

    companion object {
        val TAG = DetailsViewModel::class.java.simpleName
    }

    var item: UserItem? = null
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

    private fun getDetails(item: UserItem) {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch {
            item.url?.also { url ->
                isSwipe.value = true

                withContext(Dispatchers.IO) {
                    val response = retrofitTool.getApi().getDetails(url)
                    val result = response.body()

                    if (response.isSuccessful && result != null) {
                        result
                    } else {
                        handleError(response.errorBody())
                        null
                    }
                }?.also(::handleItem)

                isSwipe.value = false
            }
        }
    }

    private fun handleItem(item: DetailsItem) {
        handleJob?.cancel()
        handleJob = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                clear.call()
            }
            getClassFields(item, item.javaClass.superclass as Class<DetailsItem>)
            getClassFields(item, item.javaClass)
        }
    }

    private suspend fun <T> getClassFields(item: DetailsItem, clazz: Class<in T>) {
        clazz.declaredFields.asSequence().filter(::fieldFilter).forEach { field ->
            field.isAccessible = true

            if (field.annotations.any { it.annotationClass == Expose::class }) {
                val title = field.name ?: "-"
                val value = field.get(item) as? String ?: "-"
                val spanned = SpannableStringBuilder(value).apply {
                    if (URLUtil.isNetworkUrl(value)) {
                        setSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                navigate.value = value
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