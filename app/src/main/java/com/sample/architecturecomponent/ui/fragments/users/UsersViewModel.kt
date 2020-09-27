package com.sample.architecturecomponent.ui.fragments.users

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.vo.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UsersViewModel @Inject constructor(
    val context: Context,
    val retrofitTool: RetrofitTool<Api>
) : BaseViewModel() {

    companion object {
        val TAG = UsersViewModel::class.java.simpleName

        private const val DEFAULT_USER_ID = "0"
    }

    val isProgress = MutableLiveData<Boolean>()

    val isSwipe = MutableLiveData<Boolean>()

    val isResult = MutableLiveData<Boolean>()

    val results = MutableLiveData<List<UserItem>>()

    private var usersJob: Job? = null
    private var since: String = DEFAULT_USER_ID
    private val users: MutableList<UserItem> = mutableListOf()

    init {
        refresh()
    }

    fun refresh() {
        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            isResult.value = false
            isSwipe.value = true
            since = DEFAULT_USER_ID

            withContext(Dispatchers.IO) {
                val response = retrofitTool.getApi().getUsers(since)
                val result = response.body()

                if (response.isSuccessful && result != null) {
                    since = result?.last()?.id ?: DEFAULT_USER_ID
                    users.run {
                        clear()
                        addAll(result)
                        toMutableList()
                    }
                } else {
                    null
                }
            }?.also {
                results.value = it
            }

            isResult.value = true
            isSwipe.value = false
        }
    }

    fun next() {
        if (usersJob?.isActive == true) {
            return
        }

        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            isProgress.value = true

            withContext(Dispatchers.IO) {
                val response = retrofitTool.getApi().getUsers(since)
                val result = response.body()

                if (response.isSuccessful && result != null) {
                    since = result?.last()?.id ?: since
                    users.run {
                        addAll(result)
                        toMutableList()
                    }
                } else {
                    null
                }

            }?.also {
                results.value = it
            }

            isProgress.value = false
        }
    }

}