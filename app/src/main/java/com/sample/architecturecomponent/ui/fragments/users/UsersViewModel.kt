package com.sample.architecturecomponent.ui.fragments.users

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponent.R
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val context: Context,
    private val usersRepository: UsersRepository
) : BaseViewModel(context) {

    private val _isProgress = MutableStateFlow(false)
    private val _isResult = MutableStateFlow<Boolean>(false)
    private val _isSwipe = MutableStateFlow<Boolean>(false)
    private val _results = MutableStateFlow<List<User>>(emptyList())

    val isProgress: LiveData<Boolean> = _isProgress.asLiveData()
    val isResult: StateFlow<Boolean> = _isResult.asStateFlow()
    val isSwipe: LiveData<Boolean> = _isSwipe.asLiveData()
    val results: StateFlow<List<User>> = _results.asStateFlow()

    private var usersJob: Job? = null
    private var nextJob: Job? = null
    private var refreshJob: Job? = null
    private var removeJob: Job? = null
    private var addJob: Job? = null

    init {
        getUsers()
    }

    private fun getUsers() {
        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            usersRepository.getUsers()
                .onStart {
                    _isResult.tryEmit(true)
                    _isSwipe.tryEmit(false)
                    _isProgress.tryEmit(false)
                }
                .collect {
                    _isResult.tryEmit(true)
                    _isSwipe.tryEmit(false)
                    _isProgress.tryEmit(false)

                    when (it) {
                        is Data -> _results.tryEmit(it.value)
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
            _isProgress.tryEmit(true)
            usersRepository.getNextUsers().let {
                if (it is Error) {
                    handleError(it.type)
                    _isProgress.tryEmit(false)
                }
                // Like a debounce
                delay(1000)
            }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) {
            return
        }

        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _isSwipe.tryEmit(true)
            usersRepository.resetUsers().let {
                if (it is Error) {
                    handleError(it.type)
                    _isSwipe.tryEmit(false)
                }
            }
        }
    }

    fun userClick(index: Int, user: User) {
        showScreen(Screen(arg = user))
    }

    fun userLongClick(index: Int, user: User): Boolean {
        removeJob = viewModelScope.launch {
            usersRepository.removeUser(user).let {
                if (it !is Error) {
                    showMessage(Message.Action(user.toSpanned()) {
                        addUser(user)
                    })
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