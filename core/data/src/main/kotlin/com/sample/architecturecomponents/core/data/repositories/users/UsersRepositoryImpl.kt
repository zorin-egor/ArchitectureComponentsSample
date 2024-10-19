package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.startLoading
import com.sample.architecturecomponents.core.data.models.toUserEntity
import com.sample.architecturecomponents.core.data.models.toUserModels
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.UserEntity
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class UsersRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val usersDao: UsersDao,
    @IoScope private val ioScope: CoroutineScope,
) : UsersRepository {

    override fun getUsers(sinceId: Long, lastId: Long, limit: Long): Flow<Result<List<User>>> {
        val dbFlow = usersDao.getUsersUntilSinceId(sinceId = sinceId, lastId = lastId, limit = limit)
            .filterNot { it.isEmpty() }
            .map<List<UserEntity>, Result<List<User>>> { Result.Success(it.asExternalModels()) }
            .onStart { emit(Result.Loading) }
            .catch {
                Timber.e(it)
                emit(Result.Error(it))
            }

        val networkFlow = flow<Result<List<User>>> {
            emit(Result.Loading)

            val response = networkDatasource.getUsers(since = lastId, perPage = limit).getResultOrThrow()
            if (response.isNotEmpty()) {
                ioScope.launch {
                    runCatching { usersDao.insert(response.toUserEntity()) }
                        .onFailure(Timber::e)
                }
            }

            emit(Result.Success(response.toUserModels()))
        }.catch {
            Timber.e(it)
            emit(Result.Error(it))
        }

        return dbFlow.combine(networkFlow) { db, nw ->
            when {
                db is Result.Success -> db
                db is Result.Loading && nw is Result.Success -> nw
                nw is Result.Error -> nw
                else -> null
            }
        }
        .mapNotNull { it }
        .startLoading()
        .distinctUntilChanged()
    }

    override suspend fun insert(item: User) = usersDao.insert(item.toUserEntity())

    override suspend fun delete(item: User) = usersDao.delete(item.toUserEntity())

}
