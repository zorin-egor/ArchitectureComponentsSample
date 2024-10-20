package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.extensions.safeSubList
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class UsersRepositoryTestImpl : UsersRepository {

    private val users = MutableStateFlow(emptyList<User>())

    override fun getUsers(sinceId: Long, lastId: Long, limit: Long): Flow<Result<List<User>>> {
        return users.map<List<User>, Result<List<User>>> { flow ->
            val sinceIndex = flow.indexOfFirst { it.id == sinceId }
            val lastIndex = flow.indexOfFirst { it.id == lastId }
            val items = when {
                sinceIndex < 0 || lastIndex < 0 -> emptyList()
                else -> flow.safeSubList(sinceIndex + 1, (lastIndex + limit + 1).toInt())
            }
            Result.Success(items)
        }.onStart {
            emit(Result.Loading)
        }
    }

    override suspend fun insert(item: User) {
        users.update { flow -> (flow + item).distinctBy(User::id) }
    }

    override suspend fun delete(item: User) {
        users.update { flow -> flow.toMutableList().apply { remove(item) } }
    }

}