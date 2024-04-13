package com.sample.architecturecomponents.core.data.repositories.details

import com.sample.architecturecomponents.core.data.models.asExternalModel
import com.sample.architecturecomponents.core.data.models.toDetailsEntity
import com.sample.architecturecomponents.core.database.dao.DetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.database.model.toDetailsEntity
import com.sample.architecturecomponents.core.model.Details
import com.sample.architecturecomponents.core.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject

internal class DetailsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val detailsDao: DetailsDao,
    private val usersDao: UsersDao
) : DetailsRepository {

    override fun getDetails(userId: Long, url: String): Flow<Details> {
        return flow<Details> {
            Timber.d("getDetails($userId, $url)")

            Timber.d("getDetails() - db")
            detailsDao.getDetailsById(userId)
                .zip(usersDao.getUserById(userId)) { details, users ->
                    Timber.d("getDetails() - db collect: $details, $userId")
                    (details ?: users?.toDetailsEntity())?.asExternalModel()
                }
                .take(1)
                .catch { Timber.e(it) }
                .filterNotNull()
                .collect(::emit)

            Timber.d("getDetails() - network request")
            val response = runCatching { networkDatasource.getDetails(url) }.getOrNull()
            val result = response?.body()
            if (response?.isSuccessful == false || result == null) {
                Timber.d("getDetails() - end with bad network")
                return@flow
            }

            emit(result.asExternalModel())
            detailsDao.insert(result.toDetailsEntity())
            Timber.d("getDetails() - end")
        }.flowOn(Dispatchers.IO)
    }

}
