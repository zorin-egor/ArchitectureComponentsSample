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

    override fun getData(): Flow<Container<List<User>>> {
        return flow<Container<List<User>>> {
            usersDao.getUsers()
                .catch {
                    emit(Error(ErrorType.Unhandled(it)))
                }.collect { users ->
                    emit(Data(users))
                    sinceUserId = users.lastOrNull()?.userId ?: Api.DEFAULT_SINCE_ID
                }
        }.catch {
            emit(Error(ErrorType.Unknown))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun reset(): Container<List<User>> {
        return getUsers(Api.DEFAULT_SINCE_ID).also {
            if (it is Data) {
                usersDao.clearInsert(it.value)
                sinceUserId = Api.DEFAULT_SINCE_ID
            }
        }
    }

    override suspend fun next(): Container<List<User>> {
        return getUsers(sinceUserId).also {
            if (it is Data) {
                usersDao.insertAll(it.value)
            }
        }
    }

    override suspend fun add(item: User): Container<Unit> {
        return withContext(Dispatchers.IO) {
            action {
                usersDao.insert(item)
            }
        }
    }

    override suspend fun remove(item: User): Container<Unit> {
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

}
