package com.sample.architecturecomponents.core.data.repositories.user_details

import com.sample.architecturecomponents.core.data.models.toDetailsEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryModel
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.database.model.toUserDetailsEntity
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.di.IoScope
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class UserDetailsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val detailsDao: UserDetailsDao,
    private val usersDao: UsersDao,
    @IoScope private val ioScope: CoroutineScope
) : UserDetailsRepository {

    override fun getDetails(userId: Long, url: String): Flow<UserDetails> {
        return flow<UserDetails> {
            Timber.d("getDetails($userId, $url)")

            Timber.d("getDetails() - db")
            var dbUserDetails: UserDetails? = null
            detailsDao.getDetailsById(id = userId)
                .zip(usersDao.getUserById(id = userId)) { details, users ->
                    Timber.d("getDetails() - db collect: $details, $userId")
                    (details ?: users?.toUserDetailsEntity())?.asExternalModel()
                }
                .take(1)
                .catch { Timber.e(it) }
                .filterNotNull()
                .onEach { dbUserDetails = it }
                .collect(::emit)

            Timber.d("getDetails() - network request")
            val result = networkDatasource.getUserDetails(url).getResultOrThrow().toRepositoryModel()

            if (dbUserDetails != null && dbUserDetails == result) {
                Timber.d("getDetails() - db == network")
                return@flow
            }

            emit(result)

            ioScope.launch {
                runCatching { detailsDao.insert(result.toDetailsEntity()) }
                    .exceptionOrNull()?.let(Timber::e)
            }

            Timber.d("getDetails() - end")
        }
    }

}
