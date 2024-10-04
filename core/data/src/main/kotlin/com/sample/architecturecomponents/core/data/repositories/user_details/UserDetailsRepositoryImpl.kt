package com.sample.architecturecomponents.core.data.repositories.user_details

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.combineLeftFirst
import com.sample.architecturecomponents.core.common.result.startLoading
import com.sample.architecturecomponents.core.data.models.toDetailsEntity
import com.sample.architecturecomponents.core.data.models.toDetailsModel
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.database.model.toUserDetailsEntity
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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

    override fun getDetails(userId: Long, url: String): Flow<Result<UserDetails>> {
        val dbFlow = detailsDao.getDetailsById(id = userId)
            .zip(usersDao.getUserById(id = userId)) { details, users ->
                (details ?: users?.toUserDetailsEntity())?.asExternalModels()
            }
            .take(1)
            .filterNotNull()
            .map<UserDetails, Result<UserDetails>> { Result.Success(it) }
            .onStart { emit(Result.Loading) }
            .catch {
                Timber.e(it)
                emit(Result.Error(it))
            }

        val networkFlow = flow {
            emit(Result.Loading)

            val response = networkDatasource.getUserDetails(url).getResultOrThrow()

            ioScope.launch {
                runCatching { detailsDao.insert(response.toDetailsEntity()) }
                    .onFailure(Timber::e)
            }

            emit(Result.Success(response.toDetailsModel()))
        }.catch {
            Timber.e(it)
            emit(Result.Error(it))
        }

        return networkFlow.combineLeftFirst(dbFlow)
            .startLoading()
            .distinctUntilChanged()
    }

    override suspend fun insert(item: UserDetails) = detailsDao.insert(item.toDetailsEntity())

}
