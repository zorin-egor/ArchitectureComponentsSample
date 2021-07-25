package com.sample.architecturecomponent.ui.fragments.details

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.Expose
import com.sample.architecturecomponent.models.*
import com.sample.architecturecomponent.repositories.details.DetailsRepository
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.reflect.Field
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    context: Context,
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(context) {

    var item: User? = null
        set(value) {
            field = value
            value?.also(::getDetails)
        }

    private val _titles = MutableSharedFlow<Pair<String, Spanned>>(replay = 1)
    private val _clear = MutableStateFlow<Boolean>(true)
    private val _isSwipe = MutableStateFlow<Boolean>(true)

    val titles: Flow<Pair<String, Spanned>> = _titles.asSharedFlow()
    val clear: Flow<Boolean> = _clear.asSharedFlow()
    val isSwipe: LiveData<Boolean> = _isSwipe.asLiveData()

    private var detailsJob: Job? = null
    private var handleJob: Job? = null

    fun refresh() {
        item?.let(::getDetails)
    }

    private fun getDetails(item: User) {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch {
            detailsRepository.getDetails(item)
                .onStart {
                    _isSwipe.tryEmit(true)
                }
                .collect {
                    _isSwipe.tryEmit(false)
                    when (it) {
                        is Data -> handleItem(item, it.value)
                        is Empty -> handleItem(User(0), Details(0))
                        is Error -> {
                            handleItem(User(0), Details(0))
                            handleError(it.type)
                        }
                    }
                }
        }
    }

    private fun handleItem(user: User, details: Details) {
        handleJob?.cancel()
        handleJob = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _clear.tryEmit(true)
            }
            getClassFields(user, user.javaClass)
            getClassFields(details, details.javaClass.superclass as Class<*>)
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
                                showScreen(Screen(arg = value))
                            }
                        }, 0, value.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }

                withContext(Dispatchers.Main) {
                    _titles.tryEmit(Pair(title, spanned))
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