package com.sample.architecturecomponent.repositories.users

import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.api.mapTo
import com.sample.architecturecomponent.db.UsersDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val retrofitTool: RetrofitTool<Api>,
    private val usersDao: UsersDao
) : UsersRepository {

    @Volatile
    private var sinceUserId: Long = Api.DEFAULT_SINCE_ID

    @Volatile
    private var isForceUserUpdate: Boolean = false

    override fun getUsers(isForceUpdate: Boolean): Flow<Container<List<User>>> {
        return flow<Container<List<User>>> {

            isForceUserUpdate = isForceUpdate

            usersDao.getUsers()
                .catch {
                    emit(Error(ErrorType.Unhandled(it)))
                }.collect { users ->
                    when {
                        users.isEmpty() -> {
                            getUsersClear().let {
                                if (it is Error) {
                                    emit(it)
                                }
                            }
                        }
                        isForceUserUpdate -> {
                            isForceUserUpdate = false
                            getUsersClear().let {
                                if (it is Error) {
                                    emit(it)
                                }
                            }
                        }
                        else -> {
                            emit(Data(users))
                            sinceUserId = users.lastOrNull()?.userId ?: Api.DEFAULT_SINCE_ID
                        }
                    }
                }
        }.catch {
            emit(Error(ErrorType.Unknown))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getNextUsers(): Container<List<User>> {
        return getUsers(sinceUserId).also {
            if (it is Data) {
                usersDao.insertAll(it.value)
            }
        }
    }

    override suspend fun addUser(item: User): Container<Unit> {
        return withContext(Dispatchers.IO) {
            action {
                usersDao.insert(item)
            }
        }
    }

    override suspend fun removeUser(item: User): Container<Unit> {
        return withContext(Dispatchers.IO) {
            action {
                usersDao.delete(item)
            }
        }
    }

    private suspend fun getUsers(sinceId: Long): Container<List<User>> {
        return withContext(Dispatchers.IO) {
            action { retrofitTool.getApi().getUsers(sinceId) }
                .let(::mapTo)
        }
    }

    private suspend fun getUsersClear(): Container<List<User>> {
        sinceUserId = Api.DEFAULT_SINCE_ID
        return getUsers(sinceUserId).also {
            if (it is Data) {
                usersDao.clearInsert(it.value)
            }
        }
    }

}
