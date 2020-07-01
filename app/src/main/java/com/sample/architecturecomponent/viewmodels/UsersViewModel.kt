package com.sample.architecturecomponent.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.vo.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class UsersViewModel @Inject constructor(
    val context: Context,
    val retrofitTool: RetrofitTool<Api>
) : BaseViewModel() {

    companion object {
        val TAG = UsersViewModel::class.java.simpleName
    }

    val progress = MutableLiveData<Boolean>()

    val results = MutableLiveData<List<UserItem>>()

    init {
        progress.value = false
        refresh()
    }

    fun refresh() {
        viewModelScope.launch main@ {
            launch(Dispatchers.IO) work@ {
                val response = retrofitTool.getApi().getUsers(mapOf("since" to "0"))
                val result = response.body()

                if (response.isSuccessful && result != null) {
                    results.postValue(result!!)
                } else {

                }
            }
        }
    }

    fun next() {

    }

}