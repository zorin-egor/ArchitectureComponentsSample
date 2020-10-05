package com.sample.architecturecomponent.ui.fragments.users

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.api.ApiDatabaseResponse
import com.sample.architecturecomponent.api.ApiErrorResponse
import com.sample.architecturecomponent.api.ApiSuccessResponse
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import com.sample.architecturecomponent.managers.extensions.plus
import com.sample.architecturecomponent.managers.extensions.toSpanned
import com.sample.architecturecomponent.model.User
import com.sample.architecturecomponent.repository.UsersRepository
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Message
import com.sample.architecturecomponent.ui.fragments.base.Navigate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


class UsersViewModel @Inject constructor(
    val context: Context,
    val usersRepository: UsersRepository
) : BaseViewModel(context) {

    companion object {
        val TAG = UsersViewModel::class.java.simpleName
    }

    val isProgress = SingleLiveEvent<Boolean>()

    val isResult = SingleLiveEvent<Boolean>()

    val isSwipe = MutableLiveData<Boolean>()

    val results = MutableLiveData<List<User>>()

    private var usersJob: Job? = null

    init {
        refresh(false)
    }

    fun refresh(isSwipeEnabled: Boolean = true) {
        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            usersRepository.getUsers()
                .onStart {
                    isResult.value = false
                    isSwipe.value = isSwipeEnabled
                }
                .collect {
                    isResult.value = true
                    isSwipe.value = false

                    when (it) {
                        is ApiSuccessResponse -> {
                            results.value = it.value
                        }
                        is ApiDatabaseResponse -> {
                            results.value = it.value
                            message.value = Message.Text(context.getString(R.string.error_cache))
                        }
                        is ApiErrorResponse -> {
                            message.value = Message.Text(
                                it.error ?: context.getString(R.string.error_unknown)
                            )
                        }
                    }
                }
        }
    }

    fun next() {
        if (usersJob?.isActive == true) {
            return
        }

        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            usersRepository.getNextUsers()
                .onStart {
                    isProgress.value = true
                }
                .collect {
                    isProgress.value = false
                    when (it) {
                        is ApiSuccessResponse -> {
                            results.value = it.value
                        }
                        is ApiDatabaseResponse -> {
                            results.value = it.value
                            message.value = Message.Text(context.getString(R.string.error_cache))
                        }
                        is ApiErrorResponse -> {
                            message.value = Message.Text(
                                it.error ?: context.getString(R.string.error_unknown)
                            )
                        }
                    }
                }
        }
    }

    fun userClick(index: Int, user: User) {
        navigate.value = Navigate.Screen(arg = user)
    }

    fun userLongClick(index: Int, user: User): Boolean {
        viewModelScope.launch {
            usersRepository.removeUser(user)
                .collect {
                    (it as? ApiSuccessResponse)?.also {
                        results.value = it.value
                    }
                    message.value = Message.Action(user.toSpanned()) {
                        addUser(index, user)
                    }
                }
        }

        return true
    }

    private fun addUser(index: Int, item: User) {
        viewModelScope.launch {
            usersRepository.addUser(index, item)
                .collect {
                    (it as? ApiSuccessResponse)?.also {
                        results.value = it.value
                    }
                }
        }
    }

    private fun User.toSpanned(): Spanned {
        return "User ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (login ?: "-").toSpanned(context, R.color.colorAccent, Typeface.BOLD) +
                " with id ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (userId ?: "-").toString().toSpanned(context, R.color.colorAccent, Typeface.BOLD)
    }

}