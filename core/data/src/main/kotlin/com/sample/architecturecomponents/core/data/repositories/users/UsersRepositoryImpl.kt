package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.data.models.toExternalModel
import com.sample.architecturecomponents.core.data.models.toUserEntity
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.di.IoScope
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

internal class UsersRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val usersDao: UsersDao,
    private val settingsPreference: SettingsPreference,
    @IoScope private val ioScope: CoroutineScope
) : UsersRepository {

    override fun getUsers(sinceId: Long, limit: Int): Flow<List<User>> {
        return flow<List<User>> {
            Timber.d("getUsers($sinceId)")

            // For test
            settingsPreference.saveAuthToken(UUID.randomUUID().toString())

            Timber.d("getUsers() - db")
            val dbItems = mutableListOf<User>()
            usersDao.getUsersSinceId(sinceId = sinceId, limit = limit)
                .take(1)
                .catch { Timber.e(it) }
                .mapNotNull { it.asExternalModel() }
                .onEach(dbItems::addAll)
                .collect(::emit)

            Timber.d("getUsers() - network request")
            val response = networkDatasource.getUsers(since = sinceId, perPage = limit).getResultOrThrow()
            val result = response.toExternalModel()

            if (dbItems.isNotEmpty() && dbItems == result) {
                Timber.d("getUsers() - db == network")
                return@flow
            }

            emit(result)

            ioScope.launch {
                runCatching { usersDao.insertAll(response.toUserEntity()) }
                    .exceptionOrNull()?.let(Timber::e)
            }

            Timber.d("getUsers() - end")
        }
    }

    override suspend fun add(item: User) = usersDao.insert(item.toUserEntity())

    override suspend fun remove(item: User) = usersDao.delete(item.toUserEntity())

}
