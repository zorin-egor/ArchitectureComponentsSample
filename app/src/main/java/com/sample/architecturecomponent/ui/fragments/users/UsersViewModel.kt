package com.sample.architecturecomponent.ui.fragments.users

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.SingleLiveEvent
import com.sample.architecturecomponent.managers.extensions.plus
import com.sample.architecturecomponent.managers.extensions.toSpanned
import com.sample.architecturecomponent.models.Data
import com.sample.architecturecomponent.models.Error
import com.sample.architecturecomponent.models.User
import com.sample.architecturecomponent.repositories.users.UsersRepository
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModel
import com.sample.architecturecomponent.ui.fragments.base.Message
import com.sample.architecturecomponent.ui.fragments.base.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val context: Context,
    private val usersRepository: UsersRepository
) : BaseViewModel(context) {

    private val _isProgress = SingleLiveEvent<Boolean>()
    private val _isResult = SingleLiveEvent<Boolean>()
    private val _isSwipe = MutableLiveData<Boolean>()
    private val _results = MutableLiveData<List<User>>()

    val isProgress: LiveData<Boolean> = _isProgress
    val isResult: LiveData<Boolean> = _isResult
    val isSwipe: LiveData<Boolean> = _isSwipe
    val results: LiveData<List<User>> = _results

    private var usersJob: Job? = null
    private var nextJob: Job? = null
    private var removeJob: Job? = null
    private var addJob: Job? = null

    init {
        refresh(
            isSwipeEnabled = false,
            isForceUpdate = false
        )
    }

    fun refresh(isSwipeEnabled: Boolean = true, isForceUpdate: Boolean = true) {
        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            usersRepository.getUsers(isForceUpdate)
                .onStart {
                    if (_isResult.value == null) {
                        _isResult.value = false
                    }
                    _isSwipe.value = isSwipeEnabled
                }
                .collect {
                    _isSwipe.value = false
                    _isProgress.value = false

                    if (_isResult.value == false) {
                        _isResult.value = true
                    }

                    when (it) {
                        is Data -> _results.value = it.value
                        is Error -> handleError(it.type)
                    }
                }
        }
    }

    fun next() {
        if (nextJob?.isActive == true) {
            return
        }

        nextJob?.cancel()
        nextJob = viewModelScope.launch {
            _isProgress.value = true
            usersRepository.getNextUsers().let {
                if (it is Error) {
                    handleError(it.type)
                }
                delay(1000)
            }
        }
    }

    fun userClick(index: Int, user: User) {
        _navigate.value = Screen(arg = user)
    }

    fun userLongClick(index: Int, user: User): Boolean {
        removeJob = viewModelScope.launch {
            usersRepository.removeUser(user).let {
                if (it !is Error) {
                    _message.value = Message.Action(user.toSpanned()) {
                        addUser(user)
                    }
                }
            }
        }
        return true
    }

    private fun addUser(item: User) {
        addJob = viewModelScope.launch {
            usersRepository.addUser(item)
        }
    }

    private fun User.toSpanned(): Spanned {
        return "User ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (login ?: "-").toSpanned(context, R.color.colorAccent, Typeface.BOLD) +
                " with id ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (userId ?: "-").toString().toSpanned(context, R.color.colorAccent, Typeface.BOLD)
    }

}