package com.sample.architecturecomponent.ui.fragments.details

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.Expose
import com.sample.architecturecomponent.model.UserItem
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DetailsViewModel @Inject constructor(
    val context: Context
) : BaseViewModel(context) {

    companion object {
        val TAG = DetailsViewModel::class.java.simpleName
    }

    var item: UserItem? = null
        set(value) {
            field = value
            value?.also(::handleItem)
        }

    val titles = MutableLiveData<Pair<String, String>>()


    private fun handleItem(item: UserItem) {
        viewModelScope.launch(Dispatchers.IO) {
            item.javaClass.run {
                declaredFields.forEach { field ->
                    field.isAccessible = true

                    if (field.annotations.any { it.annotationClass == Expose::class }) {
                        withContext(Dispatchers.Main) {
                            titles.value = Pair(
                                field.name ?: "-",
                                field.get(item) as? String ?: "-"
                            )
                        }

                        delay(200)
                    }
                }
            }
        }
    }

}