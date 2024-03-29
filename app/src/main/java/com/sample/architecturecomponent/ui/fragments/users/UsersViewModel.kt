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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
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
    val isResult: Flow<Boolean> = _isResult.asStateFlow()
    val isSwipe: LiveData<Boolean> = _isSwipe.asLiveData()
    val results: Flow<List<User>> = _results.asStateFlow()

    private var usersJob: Job? = null
    private var nextJob: Job? = null
    private var refreshJob: Job? = null
    private var removeJob: Job? = null
    private var addJob: Job? = null

    init {
        getUsers()
        next()
    }

    private fun getUsers() {
        nextJob?.cancel()
        usersJob?.cancel()
        usersJob = viewModelScope.launch {
            usersRepository.getData()
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
                        is Data -> _results.emit(it.value)
                        is Error -> handleError(it.type)
                        else -> {}
                    }
                }
        }
    }

    fun next() {
        if (nextJob?.isActive == true) {
            return
        }

        nextJob = viewModelScope.launch {
            _isProgress.tryEmit(true)
            usersRepository.next().let {
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
            usersRepository.reset().let {
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
            usersRepository.remove(user).let {
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
            usersRepository.add(item)
        }
    }

    private fun User.toSpanned(): Spanned {
        return "User ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (login ?: "-").toSpanned(context, R.color.colorAccent, Typeface.BOLD) +
                " with id ".toSpanned(context, android.R.color.white, Typeface.BOLD) +
                (userId ?: "-").toString().toSpanned(context, R.color.colorAccent, Typeface.BOLD)
    }

}