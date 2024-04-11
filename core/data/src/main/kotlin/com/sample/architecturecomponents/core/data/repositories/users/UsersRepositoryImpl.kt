package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.data.models.asExternalModel
import com.sample.architecturecomponents.core.data.models.toUserEntity
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

internal class UsersRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val usersDao: UsersDao,
    private val settingsPreference: SettingsPreference
) : UsersRepository {

    override fun getUsers(sinceId: Long): Flow<List<User>> {
        return flow<List<User>> {
            Timber.d("getUsers($sinceId)")

            // For test
            settingsPreference.saveAuthToken(UUID.randomUUID().toString())

            usersDao.getUsersSinceId(sinceId = sinceId)
                .take(1)
                .map { it.asExternalModel() }
                .collect(::emit)

            val response = runCatching { networkDatasource.getUsers(sinceId) }.getOrNull()
            val result = response?.body()

            if (response?.isSuccessful == true && result?.isEmpty() == false) {
                emit(result.asExternalModel())
                usersDao.insertAll(result.toUserEntity())
                Timber.d("getUsers() - end with network")
                return@flow
            }

            Timber.d("getUsers() - end")
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun add(item: User) = usersDao.insert(item.toUserEntity())

    override suspend fun remove(item: User) = usersDao.delete(item.toUserEntity())

}
